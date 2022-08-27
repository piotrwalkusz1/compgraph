package com.piotrwalkusz.compgraph.injector;

import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryTest {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface FirstQualifier {
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface SecondQualifier {
    }

    public static class ClassWithFieldWithTwoQualifiers {
        @Inject
        @FirstQualifier
        @SecondQualifier
        private String value;
    }

    public static class ClassWithNoPublicConstructor {
        protected ClassWithNoPublicConstructor() {
        }
    }

    public static class ClassWithNoConstructorWithoutArgs {
        protected ClassWithNoConstructorWithoutArgs(String arg) {
        }
    }

    @Test
    void shouldThrowExceptionIfFieldHasMoreThanOneQualifierAnnotation() {
        final BeanFactory beanFactory = new BeanFactory(new Injector());

        final InjectorException exception = assertThrows(InjectorException.class, () -> beanFactory.createBean(ClassWithFieldWithTwoQualifiers.class));

        assertEquals(
                "Field \"String value\" in class ClassWithFieldWithTwoQualifiers has more than one qualifier annotation. Detected 2 qualifier annotations: FirstQualifier, SecondQualifier.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionIfClassHasNoPublicConstructor() {
        final BeanFactory beanFactory = new BeanFactory(new Injector());

        final InjectorException exception = assertThrows(InjectorException.class, () -> beanFactory.createBean(ClassWithNoPublicConstructor.class));

        assertEquals(
                "Class com.piotrwalkusz.compgraph.injector.BeanFactoryTest.ClassWithNoPublicConstructor doesn't have public no args constructor",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionIfClassHasNoConstructorWithoutArgs() {
        final BeanFactory beanFactory = new BeanFactory(new Injector());

        final InjectorException exception = assertThrows(InjectorException.class, () -> beanFactory.createBean(ClassWithNoConstructorWithoutArgs.class));

        assertEquals(
                "Class com.piotrwalkusz.compgraph.injector.BeanFactoryTest.ClassWithNoConstructorWithoutArgs doesn't have public no args constructor",
                exception.getMessage()
        );
    }
}