package no.robert.methodref;

import static java.lang.reflect.Modifier.isFinal;
import static no.robert.methodref.proxy.ProxyFactory.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * An InvocationRegistrar is a method interceptor which registers data about
 * the actual invocation that triggered the interception and any following
 * invocations on return values. The handling of the invocation data is
 * delegated to an {@link InvocationRegistry}.
 */
public class InvocationRegistrar implements MethodInterceptor {

    private final InvocationRegistry registry;

    public InvocationRegistrar(final InvocationRegistry registry) {
        this.registry = registry;
    }

    
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
    	registry.register(method, args);
    	return nextInvocationRegistrarIfPossible(method.getReturnType());
    }

    
    protected Object nextInvocationRegistrarIfPossible(Class<?> returnType) {
        return isFinal(returnType.getModifiers())? null : proxy(returnType, this);
    }
    
}