package com.piotrwalkusz.compgraph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphInjectorTest {

    public static class SimpleClass {
    }

    @Test
    void shouldNotAllowForJustInTimeBindingForClassOtherThanNode() {
        final GraphInjector graphInjector = new GraphInjector();

        final GraphException exception = assertThrows(GraphException.class, () -> graphInjector.getInstance(SimpleClass.class));

        assertEquals(
                "Just in time binding is not supported for type com.piotrwalkusz.compgraph.GraphInjectorTest.SimpleClass. JIT binding is supported only for subclasses of Node.",
                exception.getMessage()
        );
    }
}