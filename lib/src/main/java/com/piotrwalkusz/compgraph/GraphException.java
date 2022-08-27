package com.piotrwalkusz.compgraph;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class GraphException extends RuntimeException {

    public GraphException(String message) {
        super(message);
    }

    public static GraphException ambiguousSubgraphQualifiers(Field field, List<Class<? extends Annotation>> matchedSubgraphQualifiers) {
        return new GraphException(String.format(
                "Ambiguous subgraph qualifiers on field \"%s %s\" in class %s. Detected %s matched subgraph qualifiers but expected 1. Matched subgraph qualifiers: [%s].",
                field.getType().getSimpleName(),
                field.getName(),
                field.getDeclaringClass().getSimpleName(),
                matchedSubgraphQualifiers.size(),
                matchedSubgraphQualifiers.stream()
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", "))
        ));
    }

    public static GraphException noSubgraphMatchesSubgraphQualifiers(Field field, List<Class<? extends Annotation>> subgraphQualifiers) {
        return new GraphException(String.format(
                "Subgraph qualifiers on field \"%s %s\" in class %s don't match to any subgraph. Remaining subgraph qualifiers (already matched subgraph qualifiers was excluded): [%s].",
                field.getType().getSimpleName(),
                field.getName(),
                field.getDeclaringClass().getSimpleName(),
                subgraphQualifiers.stream()
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", "))
        ));
    }

    public static GraphException justInTimeBindingNotSupportedForClass(Class<?> type) {
        return new GraphException(String.format("Just in time binding is not supported for type %s. JIT binding is supported only for subclasses of Node.", type.getCanonicalName()));
    }

    public static GraphException invalidSubgraphQualifier(Class<? extends Annotation> annotationType) {
        return new GraphException(String.format(
                "The annotation %s should have meta annotation %s to be valid subgraph annotation",
                annotationType.getSimpleName(),
                SubgraphQualifier.class.getSimpleName()
        ));
    }
}
