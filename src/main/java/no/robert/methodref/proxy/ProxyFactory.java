package no.robert.methodref.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.objenesis.ObjenesisHelper;

public class ProxyFactory {

    public static <T> T proxy(Class<T> type, Callback callback) {
        callback = callback != null? callback : BLOCKER;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setCallbackType(callback.getClass());

        @SuppressWarnings("unchecked")
        Class<T> proxyClass = enhancer.createClass();

        @SuppressWarnings("unchecked")
        T proxy = (T) ObjenesisHelper.newInstance(proxyClass);
        ((Factory)proxy).setCallback(0, callback);
        return proxy;
    }

    private static final Callback BLOCKER = new MethodInterceptor() {
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
            return null;
        }
    };

    private ProxyFactory() {} static { new ProxyFactory(); }

}
