package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubgraphContainer {

    private final Map<Class<? extends Annotation>, Graph> subgraphs = new HashMap<>();

    public void addSubgraph(Class<? extends Annotation> subgraphQualifier, Graph subgraph) {
        if (!ReflectionUtils.hasAnnotation(subgraphQualifier, List.of(SubgraphQualifier.class))) {
            throw new IllegalArgumentException("Annotation must have meta annotation @SubgraphQualifier");
        }
        subgraphs.put(subgraphQualifier, subgraph);
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
}
