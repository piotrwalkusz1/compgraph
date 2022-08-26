package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.graphviz.GraphvizBuilder;
import com.piotrwalkusz.compgraph.graphviz.GraphvizCluster;
import com.piotrwalkusz.compgraph.graphviz.GraphvizEdge;
import com.piotrwalkusz.compgraph.graphviz.GraphvizNode;
import com.piotrwalkusz.compgraph.injector.Bean;
import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GraphPainter {

    private final Graph graph;
    private final GraphvizBuilder graphvizBuilder = new GraphvizBuilder();
    private final Map<Bean<?>, Integer> beansToNodesIds = new HashMap<>();
    private final Map<Bean<?>, Integer> beansToClustersIds = new HashMap<>();
    private final AtomicInteger nextNodeId = new AtomicInteger(1);
    private final AtomicInteger nextClusterId = new AtomicInteger(0);

    public GraphPainter(Graph graph) {
        this.graph = graph;
    }

    @SneakyThrows
    public void draw() {
        final List<Bean<?>> beans = graph.getInjector().getExistingBeans();
        beans.forEach(bean -> graphvizBuilder.addNode(buildNode(bean, null)));
        graph.getSubGraphsByQualifiers().forEach((qualifier, subGraph) -> graphvizBuilder.addCluster(buildCluster(qualifier.getSimpleName(), subGraph)));
        buildEdges(graph).forEach(graphvizBuilder::addEdge);

        final PrintWriter out = new PrintWriter("abc.dot", StandardCharsets.UTF_8);
        out.print(graphvizBuilder.build());
        out.close();
    }

    private GraphvizCluster buildCluster(String name, Graph graph) {
        final int clusterId = nextClusterId.getAndIncrement();
        final List<Bean<?>> beans = graph.getInjector().getExistingBeans();
        final List<GraphvizNode> nodes = beans.stream()
                .map(bean -> buildNode(bean, clusterId))
                .collect(Collectors.toList());
        final List<GraphvizCluster> clusters = graph.getSubGraphsByQualifiers().entrySet().stream()
                .map(qualifierAndSubGraph -> buildCluster(qualifierAndSubGraph.getKey().getSimpleName(), qualifierAndSubGraph.getValue()))
                .collect(Collectors.toList());

        return new GraphvizCluster(clusterId, name, nodes, clusters);
    }

    private GraphvizNode buildNode(Bean<?> bean, Integer clusterId) {
        final int nodeId = nextNodeId.getAndIncrement();
        beansToNodesIds.put(bean, nodeId);
        if (clusterId != null) {
            beansToClustersIds.put(bean, clusterId);
        }
        final GraphvizNode node = new GraphvizNode(nodeId, getNodeLabel(bean));
        node.setShape("box");

        return node;
    }

    private String getNodeLabel(Bean<?> bean) {
        final StringBuilder label = new StringBuilder();
        label.append("<<FONT>");
        label.append(escapeHtml(bean.getInstance().getClass().getSimpleName()));
        label.append("</FONT>>");

        return label.toString();
    }

    private List<GraphvizEdge> buildEdges(Graph graph) {
        final List<GraphvizEdge> result = new ArrayList<>();
        graph.getInjector().getExistingBeans().forEach(bean -> result.addAll(buildEdges(bean)));
        graph.getSubGraphs().forEach(subGraph -> result.addAll(buildEdges(subGraph)));
        return result;
    }

    private List<GraphvizEdge> buildEdges(Bean<?> bean) {
        final List<GraphvizEdge> result = new ArrayList<>();
        final int nodeId = beansToNodesIds.get(bean);
        for (Bean<?> dependency : bean.getDependencies()) {
            result.add(new GraphvizEdge(nodeId, beansToNodesIds.get(dependency), beansToClustersIds.get(dependency)));
        }

        return result;
    }

    protected String escapeHtml(String string) {
        return string.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
