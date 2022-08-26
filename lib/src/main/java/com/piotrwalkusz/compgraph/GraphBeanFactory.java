package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.injector.Bean;
import com.piotrwalkusz.compgraph.injector.BeanFactory;
import com.piotrwalkusz.compgraph.injector.KeyMatcher;
import com.piotrwalkusz.compgraph.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphBeanFactory extends BeanFactory {

    public GraphBeanFactory(GraphInjector injector) {
        super(injector);
    }

    @Override
    protected Bean<?> getBeanToInjectToField(Field field) {
        final List<Class<? extends Annotation>> subgraphQualifiers = new ArrayList<>(getSubgraphQualifiers(field));
        GraphInjector selectedInjector = (GraphInjector) injector;
        while (!subgraphQualifiers.isEmpty()) {
            final Map<Class<? extends Annotation>, Graph> subgraphsByQualifiers = selectedInjector.getSubgraphContainer().getSubgraphsByQualifiers();
            final List<Class<? extends Annotation>> matchedSubgraphQualifiers = subgraphQualifiers.stream()
                    .filter(subgraphsByQualifiers::containsKey)
                    .collect(Collectors.toList());
            if (matchedSubgraphQualifiers.size() > 1) {
                // TODO
                throw new IllegalArgumentException("Too many sub graphs.");
            }
            if (matchedSubgraphQualifiers.isEmpty()) {
                // TODO
                throw new IllegalArgumentException("No sub graph");
            }
            selectedInjector = selectedInjector.getSubgraphContainer().getSubgraph(matchedSubgraphQualifiers.get(0)).getInjector();
            subgraphQualifiers.remove(matchedSubgraphQualifiers.get(0));
        }

        final KeyMatcher<?> keyMatcher = getKeyMatcher(field);
        return selectedInjector.getBean(keyMatcher);
    }

    private List<Class<? extends Annotation>> getSubgraphQualifiers(Field field) {
        return ReflectionUtils.getAnnotationsWithAnyMetaAnnotation(field, List.of(SubgraphQualifier.class)).stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toList());
    }
}
