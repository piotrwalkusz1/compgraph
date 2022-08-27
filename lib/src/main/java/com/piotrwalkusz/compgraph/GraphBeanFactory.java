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

public final class GraphBeanFactory extends BeanFactory {

    public GraphBeanFactory(GraphInjector injector) {
        super(injector);
    }

    @Override
    protected final Bean<?> getBeanToInjectToField(Field field) {
        final List<Class<? extends Annotation>> subgraphQualifiers = new ArrayList<>(getSubgraphQualifiers(field));
        GraphInjector selectedInjector = (GraphInjector) injector;
        while (!subgraphQualifiers.isEmpty()) {
            final Map<Class<? extends Annotation>, Graph> subgraphsByQualifiers = selectedInjector.getSubgraphContainer().getSubgraphsByQualifiers();
            final List<Class<? extends Annotation>> matchedSubgraphQualifiers = subgraphQualifiers.stream()
                    .filter(subgraphsByQualifiers::containsKey)
                    .collect(Collectors.toList());
            if (matchedSubgraphQualifiers.size() > 1) {
                throw GraphException.ambiguousSubgraphQualifiers(field, matchedSubgraphQualifiers);
            }
            if (matchedSubgraphQualifiers.isEmpty()) {
                throw GraphException.noSubgraphMatchesSubgraphQualifiers(field, subgraphQualifiers);
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
