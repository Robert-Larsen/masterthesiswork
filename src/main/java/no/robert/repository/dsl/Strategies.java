package no.robert.repository.dsl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import no.robert.lambda.DefaultFieldResolver;
import no.robert.lambda.FieldResolver;
import no.robert.methodref.MethodRef;

public class Strategies {

    public static List<FieldResolver> fieldResolverStrategies;

    static {
        fieldResolverStrategies = new ArrayList<FieldResolver>();
        fieldResolverStrategies.add( new DefaultFieldResolver() );
    }

    public static Field asProperty(MethodRef methodRef) {
        Class<?> type = methodRef.getTargetType();
        for (FieldResolver fieldResolver : fieldResolverStrategies) {
            Field field = fieldResolver.resolveFrom(type, methodRef.getName());
            if (field != null)
                return field;
         }
        throw new RuntimeException("Unable to resolve field from " + methodRef.getName() + "() in " + type);
    }

}
