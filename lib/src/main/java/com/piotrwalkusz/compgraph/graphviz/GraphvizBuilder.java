package com.piotrwalkusz.compgraph.graphviz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GraphvizBuilder {

    private final List<GraphvizNode> nodes = new ArrayList<>();
    private final List<GraphvizEdge> edges = new ArrayList<>();
    private final List<GraphvizCluster> clusters = new ArrayList<>();
    private StringBuilder content;
    private Integer indentation;

    public final void addNode(GraphvizNode node) {
        nodes.add(node);
    }

    public final void addEdge(GraphvizEdge edge) {
        edges.add(edge);
    }

    public final void addCluster(GraphvizCluster subgraph) {
        clusters.add(subgraph);
    }

    public final String build() {
        content = new StringBuilder();
        indentation = 0;
        beginGraph();
        appendNodes(nodes);
        appendEdges(edges);
        appendClusters(clusters);
        endGraph();
        return content.toString();
    }

    private void beginGraph() {
        content.append("digraph G {");
        newLine();
        indentation++;
    }

    private void endGraph() {
        indentation--;
        indent();
        content.append("}");
    }

    private void appendNodes(List<GraphvizNode> nodes) {
        nodes.forEach(this::appendNode);
    }

    private void appendNode(GraphvizNode node) {
        indent();
        appendNodeIdentifier(node.getId());
        content.append(" ");
        final Map<String, String> attributes = new HashMap<>();
        attributes.put("label", node.getLabel());
        attributes.put("shape", "box");
        appendAttributes(attributes);
        newLine();
    }

    private void appendEdges(List<GraphvizEdge> edges) {
        edges.forEach(this::appendEdge);
    }

    private void appendEdge(GraphvizEdge edge) {
        indent();
        appendNodeIdentifier(edge.getTailNodeId());
        content.append(" -> ");
        appendNodeIdentifier(edge.getHeadNodeId());
        newLine();
    }

    private void appendClusters(List<GraphvizCluster> clusters) {
        clusters.forEach(this::appendCluster);
    }

    private void appendCluster(GraphvizCluster cluster) {
        indent();
        content.append("subgraph ");
        appendClusterIdentifier(cluster.getId());
        content.append(" {");
        newLine();
        indentation++;
        indent();
        content.append("label=<");
        content.append(cluster.getLabel());
        content.append(">");
        appendNodes(cluster.getNodes());
        appendClusters(cluster.getClusters());
        indentation--;
        indent();
        content.append("}");
        newLine();
    }

    private void appendNodeIdentifier(int nodeId) {
        content.append("x").append(nodeId);
    }

    private void appendClusterIdentifier(int clusterId) {
        content.append(getClusterIdentifier(clusterId));
    }

    private String getClusterIdentifier(int clusterId) {
        return "cluster" + clusterId;
    }

    private void indent() {
        content.append("    ".repeat(indentation));
    }

    private void newLine() {
        content.append("\n");
    }

    private void appendAttributes(Map<String, String> attributes) {
        beginAttributes();
        boolean first = true;
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            if (first) {
                first = false;
            } else {
                content.append(", ");
            }
            content.append(attribute.getKey());
            content.append("=");
            content.append(attribute.getValue());
        }
        endAttributes();
    }

    private void beginAttributes() {
        content.append("[");
    }

    private void endAttributes() {
        content.append("]");
    }
}
