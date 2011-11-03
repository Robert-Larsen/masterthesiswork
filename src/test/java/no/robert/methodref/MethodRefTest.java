package no.robert.methodref;

import static no.robert.methodref.MethodRef.on;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import no.robert.methodref.MethodRef;
import no.robert.methodref.exception.MustRegisterMethodFirst;

import org.junit.Test;


public class MethodRefTest {

    @Test(expected=MustRegisterMethodFirst.class)
    public void throwsExceptionIfTryingToGetMethodRefWithoutRegisteringInvocationFirst() {
        MethodRef.get();
    }


    @Test
    public void registersASingleNoArgInvocation() throws IOException {
        on(StringReader.class).read();
        MethodRef methodRef = MethodRef.get();
        assertThat(methodRef.getName(), is("read"));
        assertThat(methodRef.getArguments(), emptyArray());
        assertEquals(Integer.TYPE, methodRef.getReturnType());
    }

    @Test
    public void registersAChainOfInvocations() {
        on(Map.class).get("key").hashCode();


        MethodRef methodRef = MethodRef.get();

        System.out.println(methodRef.getName());
        System.out.println(methodRef.nextInChain().getName());
        System.out.println(methodRef.nextInChain().nextInChain().getName());

        assertThat(methodRef.getArguments(), hasItemInArray((Object)"key"));
        assertThat(methodRef.nextInChain().getName(), is("hashCode"));
        assertEquals(Map.class, methodRef.getTargetType());
        assertEquals(Object.class, methodRef.nextInChain().getTargetType());
    }

}
