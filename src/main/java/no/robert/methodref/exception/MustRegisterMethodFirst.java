package no.robert.methodref.exception;

import no.robert.methodref.MethodRef;

public class MustRegisterMethodFirst extends RuntimeException {

    public MustRegisterMethodFirst() {
        super("There has not been any method invocation registered with " +
                MethodRef.class.getSimpleName() + ".on(Class).method()");
    }
}
