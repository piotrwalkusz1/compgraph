package com.piotrwalkusz.compgraph.graphviz;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GraphvizEdge {

    private final int tailNodeId;
    private final int headNodeId;
    private final Integer headClusterId;

    public GraphvizEdge(int tailNodeId, int headNodeId) {
        this(tailNodeId, headNodeId, null);
    }
}
