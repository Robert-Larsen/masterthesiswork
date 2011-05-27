package no.robert.methodref;

import java.lang.reflect.Method;

/**
 * A registry used to store data about a specific method invocation.
 */
public interface InvocationRegistry {

    void register(Method method, Object[] args);

}
