package no.robert.methodref;

import static no.robert.methodref.proxy.ProxyFactory.proxy;

import java.lang.reflect.Method;

import no.robert.methodref.exception.MustRegisterMethodFirst;

public class MethodRef implements InvocationRegistry {

    private static final ThreadLocal<MethodRef> METHODREF = new ThreadLocal<MethodRef>();

    public static <T> T on(Class<T> type) {
    	if( METHODREF.get() == null ) {
    		MethodRef methodRef = new MethodRef();
    		METHODREF.set(methodRef);
    	}
        return proxy(type, new InvocationRegistrar(METHODREF.get()));
    }

    public static MethodRef get() {
        MethodRef methodRef = METHODREF.get();
        if (methodRef != null) {
        	METHODREF.set(null);
        	return methodRef;
        }
    throw new MustRegisterMethodFirst();
    }

    private String name;
    private Object[] arguments;
    private Class<?> targetType;
    private Class<?> returnType;
    private MethodRef nextInChain;

    private MethodRef() {}


    @Override
    public void register(Method method, Object[] args) {

        MethodRef methodRef = this;
       	for (; methodRef.nextInChain != null; methodRef = methodRef.nextInChain);
       		methodRef.name = method.getName();
       		methodRef.returnType = method.getReturnType();
       		methodRef.arguments = args;
       		methodRef.targetType = method.getDeclaringClass();
       		methodRef.nextInChain = new MethodRef();
    }


    public String getName() {
        return name;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public MethodRef nextInChain() {
        return nextInChain;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

}
