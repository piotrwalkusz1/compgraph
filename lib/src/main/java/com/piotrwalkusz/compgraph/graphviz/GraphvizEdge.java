package com.piotrwalkusz.compgraph.graphviz;

import lombok.Value;

@Value
public class GraphvizEdge {

    int tailNodeId;
    int headNodeId;
}
