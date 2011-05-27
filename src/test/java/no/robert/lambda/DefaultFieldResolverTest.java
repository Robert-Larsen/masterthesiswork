package no.robert.lambda;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Test;

public class DefaultFieldResolverTest
{
    private Class bookClass = new Book().getClass();
    private Class authorClass = new Author().getClass();
    private FieldResolver fieldResolver = new DefaultFieldResolver();
    
    @Test
    public void getTest()
    {
        Method method = null;
        try
        {
            method = bookClass.getMethod( "getTitle" );
        }
        catch ( SecurityException e )
        {
            e.printStackTrace();
        }
        catch ( NoSuchMethodException e )
        {
            e.printStackTrace();
        }
        Field f = fieldResolver.resolveFrom( bookClass, method  );

        assertThat( f.getName(), is( "title" ) );
    }
    
    @Test
    public void nonExistingMethodTest()
    {
        Method method = null;
        try
        {
            method = bookClass.getMethod(  "getTitle" );
        }
        catch ( SecurityException e )
        {
            e.printStackTrace();
        }
        catch ( NoSuchMethodException e )
        {
            e.printStackTrace();
        }
        Field f = fieldResolver.resolveFrom( authorClass, method  );
        
        assertNull( f );
    }
    
    @Test
    public void isTest()
    {
        Method method = null;
        try
        {
            method = bookClass.getMethod(  "isAvailable" );
        }
        catch ( SecurityException e )
        {
            e.printStackTrace();
        }
        catch ( NoSuchMethodException e )
        {
            e.printStackTrace();
        }
        Field f = fieldResolver.resolveFrom( bookClass, method  );

        assertThat( f.getName(), is( "available" ) );        
    }
    
    @Test
    public void hasTest()
    {
        Method method = null;
        try
        {
            method = bookClass.getMethod(  "hasPaperback" );
        }
        catch ( SecurityException e )
        {
            e.printStackTrace();
        }
        catch ( NoSuchMethodException e )
        {
            e.printStackTrace();
        }
        Field f = fieldResolver.resolveFrom( bookClass, method  );

        assertThat( f.getName(), is( "isPaperback" ) );       
    }

}
