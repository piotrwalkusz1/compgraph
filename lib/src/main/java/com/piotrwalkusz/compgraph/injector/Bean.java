package com.piotrwalkusz.compgraph.injector;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;

@Getter
@AllArgsConstructor
public final class Bean<T> {

    private final T instance;
    private final Class<? extends Annotation> annotationType;

    public Bean(T instance) {
        this(instance, null);
    }

    public Class<?> getType() {
        return instance.getClass();
    }
}