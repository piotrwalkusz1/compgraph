package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.injector.Bean;
import com.piotrwalkusz.compgraph.injector.BeanFactory;
import com.piotrwalkusz.compgraph.injector.KeyMatcher;
import com.piotrwalkusz.compgraph.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GraphBeanFactory extends BeanFactory {

    private final Graph graph;

    public GraphBeanFactory(Graph graph) {
        super(graph.getInjector());
        this.graph = graph;
    }

    @Override
    protected Bean<?> getBeanToInjectToField(Field field) {
        final List<Class<? extends Annotation>> subGraphQualifiers = new ArrayList<>(getSubGraphQualifiers(field));
        final AtomicReference<Graph> selectedGraph = new AtomicReference<>(graph);
        while (!subGraphQualifiers.isEmpty()) {
            final List<Class<? extends Annotation>> matchedSubGraphQualifiers = subGraphQualifiers.stream()
                    .filter(subGraphQualifier -> selectedGraph.get().getSubGraph(subGraphQualifier) != null)
                    .collect(Collectors.toList());
            if (matchedSubGraphQualifiers.size() > 1) {
                // TODO
                throw new IllegalArgumentException("Too many sub graphs.");
            }
            if (matchedSubGraphQualifiers.isEmpty()) {
                // TODO
                throw new IllegalArgumentException("No sub graph");
            }
            selectedGraph.set(selectedGraph.get().getSubGraph(matchedSubGraphQualifiers.get(0)));
            subGraphQualifiers.remove(matchedSubGraphQualifiers.get(0));
        }

        final KeyMatcher<?> keyMatcher = getKeyMatcher(field);
        return selectedGraph.get().getInjector().getBean(keyMatcher);
    }

    private List<Class<? extends Annotation>> getSubGraphQualifiers(Field field) {
        return ReflectionUtils.getAnnotationsWithAnyMetaAnnotation(field, List.of(SubGraphQualifier.class)).stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toList());
    }
}
