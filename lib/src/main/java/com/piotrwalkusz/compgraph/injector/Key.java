package com.piotrwalkusz.compgraph.injector;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.lang.annotation.Annotation;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Key<T> {

    Class<T> type;
    Class<? extends Annotation> annotationType;

    public static <T> Key<T> of(Class<T> type) {
        return of(type, null);
    }

    public static <T> Key<T> of(Class<T> type, Class<? extends Annotation> annotationType) {
        return new Key<>(type, annotationType);
    }

    @Override
    public final String toString() {
        return String.format("{type: %s, annotationType: %s}", type.getCanonicalName(), annotationType == null ? null : annotationType.getCanonicalName());
    }
}
