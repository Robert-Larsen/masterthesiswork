package no.robert.lambda;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface FieldResolver
{
    Field resolveFrom( Class<?> type, Method method );
    
    Field resolveFrom( Class<?> type, String methodname );
}
