package com.piotrwalkusz.compgraph;

import lombok.AccessLevel;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class Graph {

    @Getter(AccessLevel.PACKAGE)
    private final GraphInjector injector;

    public Graph() {
        this(null);
    }

    public Graph(Graph parentGraph) {
        this.injector = new GraphInjector(parentGraph == null ? null : parentGraph.injector);
    }

    public final Graph addInput(Object input) {
        return addInput(input, null);
    }

    public final Graph addInput(Object input, Class<? extends Annotation> annotationType) {
        injector.bind(input, annotationType);
        return this;
    }

    public final Graph addNode(Class<?> nodeType) {
        return addNode(nodeType, null);
    }

    public final Graph addNode(Class<?> nodeType, Class<? extends Annotation> annotationType) {
        injector.bind(nodeType, annotationType).toSelf();
        return this;
    }

    public final Graph addSubgraph(Class<? extends Annotation> subgraphQualifier, Consumer<Graph> setupGraph) {
        final Graph subgraph = new Graph(this);
        if (setupGraph != null) {
            setupGraph.accept(subgraph);
        }
        injector.getSubgraphContainer().addSubgraph(subgraphQualifier, subgraph);
        return this;
    }

    public final Graph getSubgraph(Class<? extends Annotation> subgraphQualifier) {
        return injector.getSubgraphContainer().getSubgraph(subgraphQualifier);
    }

    public final List<Graph> getSubgraphs() {
        return injector.getSubgraphContainer().getSubgraphs();
    }

    public final Map<Class<? extends Annotation>, Graph> getSubgraphsByQualifiers() {
        return injector.getSubgraphContainer().getSubgraphsByQualifiers();
    }

    public final <T> T evaluate(Class<? extends Node<T>> type) {
        return getInstance(type).getValue();
    }

    public final <T> T evaluate(Class<? extends Node<T>> type, Class<? extends Annotation> annotationType) {
        return getInstance(type, annotationType).getValue();
    }

    public final <T> T getInstance(Class<? extends T> type) {
        return injector.getInstance(type);
    }

    public final <T> T getInstance(Class<? extends T> type, Class<? extends Annotation> annotationType) {
        return injector.getInstance(type, annotationType);
    }

    public final String draw() {
        return new GraphPainter(this).draw();
    }
}
