package no.robert.lambda;

import static org.apache.commons.lang.StringUtils.lowerCase;
import static org.apache.commons.lang.StringUtils.capitalize;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class DefaultFieldResolver implements FieldResolver
{
    @Override
    public Field resolveFrom( Class<?> type, Method method )
    {
        Field[] fields = type.getDeclaredFields();
        String methodname = method.getName();
        
        String methodnameLower = lowerCase( methodname );
        for( Field f : fields )
        {
            String fieldname = f.getName();

            if( methodname.matches( "get\\w{1,}" ) )
            {                
                if( methodnameLower.substring( 3 ).equals( fieldname ) )
                    return f;                
            }
            else if( methodname.matches( "is\\w{1,}" ) )
            {
                if( methodname.equals( fieldname ) || methodnameLower.substring( 2 ).equals( fieldname ) )
                    return f;
            }
            else if( methodname.matches( "has\\w{1,}" ) )
            {
                if( fieldname.matches( "is\\w{1,}" ) )
                    fieldname = lowerCase( fieldname.substring( 2 ) );

                if( methodnameLower.substring( 3 ).equals( fieldname ) )
                    return f;
            }
        }
        return null;
    }
    
    @Override
    public Field resolveFrom( Class<?> type, String method )
    {
        Field[] fields = type.getDeclaredFields();
        String methodname = method;
        
        String methodnameLower = lowerCase( methodname );
        for( Field f : fields )
        {
            String fieldname = f.getName();

            if( methodname.matches( "get\\w{1,}" ) )
            {                
                if( methodnameLower.substring( 3 ).equals( fieldname ) )
                    return f;                
            }
            else if( methodname.matches( "is\\w{1,}" ) )
            {
                if( methodname.equals( fieldname ) || methodnameLower.substring( 2 ).equals( fieldname ) )
                    return f;
            }
            else if( methodname.matches( "has\\w{1,}" ) )
            {
                if( fieldname.matches( "is\\w{1,}" ) )
                    fieldname = lowerCase( fieldname.substring( 2 ) );

                if( methodnameLower.substring( 3 ).equals( fieldname ) )
                    return f;
            }
        }
        return null;
    }

}
