package com.piotrwalkusz.compgraph.grapher;

import com.google.inject.grapher.NodeId;
import com.google.inject.grapher.graphviz.GraphvizNode;

public class CustomGraphvizNode extends GraphvizNode {

    private String annotation;
    private String value;

    public CustomGraphvizNode(NodeId nodeId) {
        super(nodeId);
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
