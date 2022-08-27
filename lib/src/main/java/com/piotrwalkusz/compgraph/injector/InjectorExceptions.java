package com.piotrwalkusz.compgraph.injector;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InjectorExceptions {

    public static InjectorException fieldHasMoreThanOneQualifier(Field field, List<Annotation> qualifierAnnotations) {
        return new InjectorException(String.format(
                "Field \"%s %s\" in class %s has more than one qualifier annotation. Detected %s qualifier annotations: %s.",
                field.getType().getSimpleName(),
                field.getName(),
                field.getDeclaringClass().getSimpleName(),
                qualifierAnnotations.size(),
                qualifierAnnotations.stream()
                        .map(Annotation::annotationType)
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", "))
        ));
    }

    public static InjectorException noPublicNoArgsConstructor(Class<?> type) {
        return new InjectorException(String.format("Class %s doesn't have public no args constructor", type.getCanonicalName()));
    }

    public static <T> InjectorException foundMoreThanOneBinding(KeyMatcher<?> keyMatcher, List<Binding<? extends T>> bindings) {
        return new InjectorException(String.format(
                "Found more than one binding for %s. Found %s bindings: %s.",
                keyMatcher,
                bindings.size(),
                bindings.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", "))
        ));
    }
}
