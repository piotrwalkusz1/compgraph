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
    private final Map<Class<? extends Annotation>, Graph> subgraphs;

    public Graph() {
        this(null);
    }

    public Graph(Graph parentGraph) {
        this.injector = new GraphInjector(this, parentGraph == null ? null : parentGraph.injector);
        this.subgraphs = new HashMap<>();
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

    public Graph addSubgraph(Class<? extends Annotation> subgraphQualifier, Consumer<Graph> setupGraph) {
        if (!ReflectionUtils.hasAnnotation(subgraphQualifier, List.of(SubgraphQualifier.class))) {
            throw new IllegalArgumentException("Annotation must have meta annotation @SubgraphQualifier");
        }
        final Graph subgraph = new Graph(this);
        if (setupGraph != null) {
            setupGraph.accept(subgraph);
        }
        subgraphs.put(subgraphQualifier, subgraph);
        return this;
    }

    public Graph getSubgraph(Class<? extends Annotation> subgraphQualifier) {
        return subgraphs.get(subgraphQualifier);
    }

    public List<Graph> getSubgraphs() {
        return new ArrayList<>(subgraphs.values());
    }

    public Map<Class<? extends Annotation>, Graph> getSubgraphsByQualifiers() {
        return new HashMap<>(subgraphs);
    }

    public <T> T evaluate(Class<? extends Node<T>> type) {
        return getInstance(type).getValue();
    }

    public <T> T evaluate(Class<? extends Node<T>> type, Class<? extends Annotation> annotationType) {
        return getInstance(type, annotationType).getValue();
    }

    public <T> T getInstance(Class<? extends T> type) {
        return injector.getInstance(type);
    }

    public <T> T getInstance(Class<? extends T> type, Class<? extends Annotation> annotationType) {
        return injector.getInstance(type, annotationType);
    }

    public void draw() {
        new GraphPainter(this).draw();
    }
}
