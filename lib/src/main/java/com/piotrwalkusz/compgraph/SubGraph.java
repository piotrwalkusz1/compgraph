package com.piotrwalkusz.compgraph;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class SubGraph {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PACKAGE)
    private Graph graph;

    protected abstract void setupGraph(Graph graph);
}
