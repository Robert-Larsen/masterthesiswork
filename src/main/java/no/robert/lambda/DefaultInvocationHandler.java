package no.robert.lambda;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

public class DefaultInvocationHandler<T> implements InvocationHandler
{       
    protected static final ThreadLocal<List<Method>> lastMethod = new ThreadLocal<List<Method>>();
    protected static final ThreadLocal<List<Class<?>>> lastType = new ThreadLocal<List<Class<?>>>();
        
    public DefaultInvocationHandler( Class<?> type )
    {
        if( lastType.get() == null )
            lastType.set( new ArrayList<Class<?>>() );
        if( !lastType.get().contains( type ) )
            lastType.get().add( type );
        
        lastMethod.set( new ArrayList<Method>() );
    } 
    
    public DefaultInvocationHandler( Class<?> type, boolean isFinal )
    {
        if( lastType.get() == null )
            lastType.set( new ArrayList<Class<?>>() );
        if( !lastType.get().contains( type ) )
            lastType.get().add( type );

        if( isFinal )
        {
            if( lastMethod.get() == null )
                lastMethod.set( new ArrayList<Method>() );
        }        
    } 
    
    
    @SuppressWarnings( "unchecked" )
    @Override
    public Object invoke( Object arg0, Method method, Object[] arg2 ) throws Throwable
    {    
        if( !lastMethod.get().contains( method ) )
            lastMethod.get().add( method );
  
        if( !Modifier.isFinal( method.getReturnType().getModifiers() ) )
        {                       
            return ( T ) Enhancer.create( method.getReturnType(), new DefaultInvocationHandler<T>( method.getReturnType(), true ) );
        }
        else if( method.getReturnType().isPrimitive() )
        {   
            String typeName = method.getReturnType().getSimpleName();
            if( typeName.equals( "int" ) )
                return Integer.MIN_VALUE;
            else if( typeName.equals( "double" ) )
                return Double.MIN_VALUE;
            else if( typeName.equals( "long" ) )
                return Long.MIN_VALUE;
            else if( typeName.equals( "float" ) )
                return Float.MAX_VALUE;
            else if( typeName.equals( "short" ) )
                return Short.MIN_VALUE;
            else if( typeName.equals( "boolean" ) )
                return false;
            else if( typeName.equals( "byte" ) )
                return Byte.MIN_VALUE;
            else if( typeName.equals( "char" ) )
                return Character.MIN_VALUE;
        }        
        return null;
    }
}
