package com.piotrwalkusz.compgraph;

import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphTest {

    @SubgraphQualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface FirstSubgraph {
    }

    @SubgraphQualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface SecondSubgraph {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NotSubgraphQualifier {
    }

    public static class ClassWithFieldWithTwoSubgraphQualifiers {
        @Inject
        @FirstSubgraph
        @SecondSubgraph
        public String value;
    }

    @Test
    void shouldSupportManySubgraphQualifiers() {
        final Graph graph = new Graph()
                .addNode(ClassWithFieldWithTwoSubgraphQualifiers.class)
                .addSubgraph(FirstSubgraph.class, firstSubgraph -> firstSubgraph
                        .addSubgraph(SecondSubgraph.class, secondSubgraph -> secondSubgraph.addInput("TestInstance")));

        assertEquals("TestInstance", graph.getInstance(ClassWithFieldWithTwoSubgraphQualifiers.class).value);
    }

    @Test
    void shouldThrowExceptionIfSubgraphQualifiersAreAmbiguous() {
        final Graph graph = new Graph()
                .addNode(ClassWithFieldWithTwoSubgraphQualifiers.class)
                .addSubgraph(FirstSubgraph.class, null)
                .addSubgraph(SecondSubgraph.class, null);

        final GraphException exception = assertThrows(GraphException.class, () -> graph.getInstance(ClassWithFieldWithTwoSubgraphQualifiers.class));

        assertEquals(
                "Ambiguous subgraph qualifiers on field \"String value\" in class ClassWithFieldWithTwoSubgraphQualifiers. Detected 2 matched subgraph qualifiers but expected 1. Matched subgraph qualifiers: [FirstSubgraph, SecondSubgraph].",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionIfNoSubgraphMatchesSubgraphQualifiers() {
        final Graph graph = new Graph()
                .addNode(ClassWithFieldWithTwoSubgraphQualifiers.class)
                .addSubgraph(FirstSubgraph.class, null);

        final GraphException exception = assertThrows(GraphException.class, () -> graph.getInstance(ClassWithFieldWithTwoSubgraphQualifiers.class));

        assertEquals(
                "Subgraph qualifiers on field \"String value\" in class ClassWithFieldWithTwoSubgraphQualifiers don't match to any subgraph. Remaining subgraph qualifiers (already matched subgraph qualifiers was excluded): [SecondSubgraph].",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionIfSubgraphQualifierDoesNotHaveProperMetaAnnotation() {
        final GraphException exception = assertThrows(GraphException.class, () -> new Graph().addSubgraph(NotSubgraphQualifier.class, null));

        assertEquals("The annotation NotSubgraphQualifier should have meta annotation SubgraphQualifier to be valid subgraph annotation", exception.getMessage());
    }
}