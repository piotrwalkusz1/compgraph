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

    public Graph addInput(Object input) {
        return addInput(input, null);
    }

    public Graph addInput(Object input, Class<? extends Annotation> annotationType) {
        injector.bind(input, annotationType);
        return this;
    }

    public Graph addNode(Class<? extends Node<?>> nodeType) {
        return addNode(nodeType, null);
    }

    public Graph addNode(Class<? extends Node<?>> nodeType, Class<? extends Annotation> annotationType) {
        injector.bind(nodeType, annotationType).toSelf();
        return this;
    }

    public Graph addSubgraph(Class<? extends Annotation> subgraphQualifier, Consumer<Graph> setupGraph) {
        final Graph subgraph = new Graph(this);
        if (setupGraph != null) {
            setupGraph.accept(subgraph);
        }
        injector.getSubgraphContainer().addSubgraph(subgraphQualifier, subgraph);
        return this;
    }

    public Graph getSubgraph(Class<? extends Annotation> subgraphQualifier) {
        return injector.getSubgraphContainer().getSubgraph(subgraphQualifier);
    }

    public List<Graph> getSubgraphs() {
        return injector.getSubgraphContainer().getSubgraphs();
    }

    public Map<Class<? extends Annotation>, Graph> getSubgraphsByQualifiers() {
        return injector.getSubgraphContainer().getSubgraphsByQualifiers();
    }

    public <T> T evaluate(Class<? extends Node<T>> type) {
        return getInstance(type).getValue();
    }

    public <T> T evaluate(Class<? extends Node<T>> type, Class<? extends Annotation> annotationType) {
        return getInstance(type, annotationType).getValue();
    }

    public <T> T getInstance(Class<? extends T> type) {
        return injector.getInstance(type);
    }

    public <T> T getInstance(Class<? extends T> type, Class<? extends Annotation> annotationType) {
        return injector.getInstance(type, annotationType);
    }

    public void draw() {
        new GraphPainter(this).draw();
    }
}
