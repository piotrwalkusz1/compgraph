package com.piotrwalkusz.compgraph;

public abstract class SubGraph {

    private Graph graph;

    protected Graph getGraph() {
        return graph;
    }

    void setGraph(Graph graph) {
        this.graph = graph;
    }

    protected abstract void configure(GraphBuilder graphBuilder);
}
