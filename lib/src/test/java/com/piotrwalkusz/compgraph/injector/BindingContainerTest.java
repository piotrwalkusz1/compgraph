package com.piotrwalkusz.compgraph.injector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BindingContainerTest {

    @Test
    void shouldThrowExceptionIfFoundMoreThanOneBinding() {
        final Injector injector = new Injector();
        final BindingContainer bindingContainer = new BindingContainer();
        bindingContainer.addBinding(new Binding<>(Key.of(String.class), new ConstantProvider<>(new Bean<>("I1"))));
        bindingContainer.addBinding(new Binding<>(Key.of(String.class), new CreatingProvider<>(String.class, injector)));

        final InjectorException exception = assertThrows(InjectorException.class, () -> bindingContainer.getBinding(KeyMatcher.of(String.class)));

        assertEquals(
                "Found more than one binding for {type: java.lang.String, annotationType: null}. Found 2 bindings: [{key: {type: java.lang.String, annotationType: null}, provider: <instance of java.lang.String>}, {key: {type: java.lang.String, annotationType: null}, provider: <provider of java.lang.String>}].",
                exception.getMessage()
        );
    }
}