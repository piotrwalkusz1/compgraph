package com.piotrwalkusz.compgraph.graphviz;

import lombok.Data;

@Data
public class GraphvizNode {

    private final int id;
    private final String label;
    private String shape;
}
