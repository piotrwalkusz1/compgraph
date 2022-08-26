package com.piotrwalkusz.compgraph.injector;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.Objects;

@Data
@AllArgsConstructor
public class KeyMatcher<T> {

    private final Class<T> type;
    private final Class<? extends Annotation> annotationType;

    public KeyMatcher(Class<T> type) {
        this(type, null);
    }

    public boolean match(Key<?> key) {
        return type.isAssignableFrom(key.getType()) && Objects.equals(annotationType, key.getAnnotationType());
    }

    public Key<T> getKeyThatSatisfyMatcher() {
        return new Key<>(type, annotationType);
    }
}
