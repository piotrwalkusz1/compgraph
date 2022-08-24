package com.piotrwalkusz.compgraph;

import java.lang.annotation.Annotation;

public final class Graph {

    private final GraphInjector container;

    public Graph() {
        this(null);
    }

    public Graph(Graph parentGraph) {
        this.container = parentGraph == null
                ? new GraphInjector()
                : new GraphInjector(parentGraph.container);
    }

    public Graph addInput(Object input) {
        return addInput(input, null);
    }

    public Graph addInput(Object input, Class<? extends Annotation> annotationType) {
        container.addInstance(input, annotationType);
        return this;
    }

    public Graph addSubGraph(SubGraph subGraph) {
        container.addInstance(subGraph);
        subGraph.setGraph(new Graph(this));
        subGraph.setupGraph(subGraph.getGraph());
        return this;
    }

    public <T> T evaluate(Class<? extends Node<T>> node) {
        return container.getInstance(node).getValue();
    }
}
