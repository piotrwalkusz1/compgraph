package com.piotrwalkusz.compgraph;

import lombok.AccessLevel;
import lombok.Getter;

import java.lang.annotation.Annotation;

public final class Graph {

    @Getter(AccessLevel.PACKAGE)
    private final GraphInjector injector;

    public Graph() {
        this(null);
    }

    public Graph(Graph parentGraph) {
        this.injector = parentGraph == null
                ? new GraphInjector()
                : new GraphInjector(parentGraph.injector);
    }

    public Graph addInput(Object input) {
        return addInput(input, null);
    }

    public Graph addInput(Object input, Class<? extends Annotation> annotationType) {
        injector.bind(input, annotationType);
        return this;
    }

    public Graph addSubGraph(SubGraph subGraph) {
        injector.bind(subGraph);
        subGraph.setGraph(new Graph(this));
        subGraph.setupGraph(subGraph.getGraph());
        return this;
    }

    public <T> T evaluate(Class<? extends Node<T>> node) {
        return injector.getInstance(node).getValue();
    }

    public void draw() {
        new GraphPainter(this).draw();
    }
}
