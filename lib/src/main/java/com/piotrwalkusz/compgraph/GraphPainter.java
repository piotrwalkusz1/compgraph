package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.graphviz.GraphvizBuilder;
import com.piotrwalkusz.compgraph.graphviz.GraphvizCluster;
import com.piotrwalkusz.compgraph.graphviz.GraphvizEdge;
import com.piotrwalkusz.compgraph.graphviz.GraphvizNode;
import com.piotrwalkusz.compgraph.injector.Bean;
import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GraphPainter {

    private final Graph graph;
    private final GraphvizBuilder graphvizBuilder = new GraphvizBuilder();
    private final Map<Bean<?>, Integer> beansToNodesIds = new HashMap<>();
    private final Map<Bean<?>, Integer> beansToClustersIds = new HashMap<>();
    private final AtomicInteger nextNodeId = new AtomicInteger(1);
    private final AtomicInteger nextClusterId = new AtomicInteger(1);

    public GraphPainter(Graph graph) {
        this.graph = graph;
    }

    @SneakyThrows
    public void draw() {
        final List<Bean<?>> beans = graph.getInjector().getBeans();
        beans.forEach(bean -> {
            if (bean.getInstance() instanceof SubGraph) {
                graphvizBuilder.addCluster(buildCluster((Bean<SubGraph>) bean));
            } else {
                graphvizBuilder.addNode(buildNode(bean, null));
            }
        });
        buildEdges(beans).forEach(graphvizBuilder::addEdge);

        final PrintWriter out = new PrintWriter("abc.dot", StandardCharsets.UTF_8);
        out.print(graphvizBuilder.build());
        out.close();
    }

    private GraphvizCluster buildCluster(Bean<SubGraph> root) {
        final int clusterId = nextClusterId.getAndIncrement();
        final List<GraphvizNode> nodes = new ArrayList<>();
        final List<GraphvizCluster> clusters = new ArrayList<>();

        nodes.add(buildNode(root, clusterId));

        for (Bean<?> bean : root.getInstance().getGraph().getInjector().getBeans()) {
            if (bean.getInstance() instanceof SubGraph) {
                clusters.add(buildCluster((Bean<SubGraph>) bean));
            } else {
                nodes.add(buildNode(bean, clusterId));
            }
        }

        return new GraphvizCluster(clusterId, "Cluster", nodes, clusters);
    }

    private GraphvizNode buildNode(Bean<?> bean, Integer clusterId) {
        final int nodeId = nextNodeId.getAndIncrement();
        beansToNodesIds.put(bean, nodeId);
        if (clusterId != null) {
            beansToClustersIds.put(bean, clusterId);
        }
        final GraphvizNode node = new GraphvizNode(nodeId, getNodeLabel(bean));
        node.setShape(bean.getInstance() instanceof SubGraph ? "plain" : "box");

        return node;
    }

    private String getNodeLabel(Bean<?> bean) {
        final StringBuilder label = new StringBuilder();
        label.append("<<FONT>");
        label.append(escapeHtml(bean.getType().getSimpleName()));
        if (bean.getInstance() instanceof DisplayableValue) {
            label.append("<BR/>");
            label.append(escapeHtml(((DisplayableValue) bean.getInstance()).getDisplayedValue()));
        }
        label.append("</FONT>>");

        return label.toString();
    }

    private List<GraphvizEdge> buildEdges(List<Bean<?>> beans) {
        return beans.stream()
                .map(this::buildEdges)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<GraphvizEdge> buildEdges(Bean<?> bean) {
        final List<GraphvizEdge> result = new ArrayList<>();
        if (bean.getInstance() instanceof SubGraph) {
            final List<Bean<?>> beans = ((SubGraph) bean.getInstance()).getGraph().getInjector().getBeans();
            result.addAll(buildEdges(beans));
        } else {
            final int nodeId = beansToNodesIds.get(bean);
            for (Bean<?> dependency : bean.getDependencies()) {
                result.add(new GraphvizEdge(nodeId, beansToNodesIds.get(dependency), beansToClustersIds.get(dependency)));
            }
        }

        return result;
    }

    protected String escapeHtml(String string) {
        return string.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
