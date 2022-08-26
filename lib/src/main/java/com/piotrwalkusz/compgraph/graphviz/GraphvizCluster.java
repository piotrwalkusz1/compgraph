package com.piotrwalkusz.compgraph.graphviz;

import lombok.Value;

import java.util.List;

@Value
public class GraphvizCluster {

    int id;
    String label;
    List<GraphvizNode> nodes;
    List<GraphvizCluster> clusters;
}
