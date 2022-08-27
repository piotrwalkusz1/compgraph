package com.piotrwalkusz.compgraph.injector;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.lang.annotation.Annotation;
import java.util.Objects;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyMatcher<T> {

    Class<T> type;
    Class<? extends Annotation> annotationType;

    public static <T> KeyMatcher<T> of(Class<T> type) {
        return of(type, null);
    }

    public static <T> KeyMatcher<T> of(Class<T> type, Class<? extends Annotation> annotationType) {
        return new KeyMatcher<>(type, annotationType);
    }

    public final boolean match(Key<?> key) {
        return type.isAssignableFrom(key.getType()) && Objects.equals(annotationType, key.getAnnotationType());
    }

    public final Key<T> getKeyThatSatisfyMatcher() {
        return Key.of(type, annotationType);
    }

    @Override
    public final String toString() {
        return String.format("{type: %s, annotationType: %s}", type.getCanonicalName(), annotationType == null ? null : annotationType.getCanonicalName());
    }
}
