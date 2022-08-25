package com.piotrwalkusz.compgraph.graphviz;

import lombok.Data;

import java.util.List;

@Data
public class GraphvizCluster {

    private final int id;
    private final String label;
    private final List<GraphvizNode> nodes;
    private final List<GraphvizCluster> clusters;
}
