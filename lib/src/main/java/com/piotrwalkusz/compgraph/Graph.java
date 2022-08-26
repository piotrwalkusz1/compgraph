package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class Graph {

    @Getter(AccessLevel.PACKAGE)
    private final GraphInjector injector;
    private final Map<Class<? extends Annotation>, Graph> subGraphs;

    public Graph() {
        this(null);
    }

    public Graph(Graph parentGraph) {
        this.injector = new GraphInjector(this, parentGraph == null ? null : parentGraph.injector);
        this.subGraphs = new HashMap<>();
    }

    public Graph addInput(Object input) {
        return addInput(input, null);
    }

    public Graph addInput(Object input, Class<? extends Annotation> annotationType) {
        injector.bind(input, annotationType);
        return this;
    }

    public Graph addNode(Class<? extends Node<?>> nodeType) {
        return addNode(nodeType, null);
    }

    public Graph addNode(Class<? extends Node<?>> nodeType, Class<? extends Annotation> annotationType) {
        injector.bind(nodeType, annotationType).toSelf();
        return this;
    }

    public Graph addSubGraph(Class<? extends Annotation> subGraphQualifier) {
        return addSubGraph(subGraphQualifier, null);
    }

    public Graph addSubGraph(Class<? extends Annotation> subGraphQualifier, Consumer<Graph> setupGraph) {
        if (!ReflectionUtils.hasAnnotation(subGraphQualifier, List.of(SubGraphQualifier.class))) {
            throw new IllegalArgumentException("Annotation must have meta annotation @SubGraphQualifier");
        }
        final Graph subGraph = new Graph(this);
        if (setupGraph != null) {
            setupGraph.accept(subGraph);
        }
        subGraphs.put(subGraphQualifier, subGraph);
        return this;
    }

    public Graph getSubGraph(Class<? extends Annotation> subGraphQualifier) {
        return subGraphs.get(subGraphQualifier);
    }

    public List<Graph> getSubGraphs() {
        return new ArrayList<>(subGraphs.values());
    }

    public Map<Class<? extends Annotation>, Graph> getSubGraphsByQualifiers() {
        return new HashMap<>(subGraphs);
    }

    public <T> T evaluate(Class<? extends Node<T>> node) {
        return injector.getInstance(node).getValue();
    }

    public void draw() {
        new GraphPainter(this).draw();
    }
}
