package com.piotrwalkusz.compgraph.injector;

import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public final class Bean<T> {

    private final T instance;
    private final Class<? extends Annotation> annotationType;
    private final List<Bean<?>> dependencies;

    public Bean(T instance) {
        this(instance, null, null);
    }

    public Bean(T instance, Class<? extends Annotation> annotationType) {
        this(instance, annotationType, null);
    }

    public Bean(T instance, List<Bean<?>> dependencies) {
        this(instance, null, dependencies);
    }

    private Bean(T instance, Class<? extends Annotation> annotationType, List<Bean<?>> dependencies) {
        this.instance = instance;
        this.annotationType = annotationType;
        this.dependencies = dependencies == null ? Collections.emptyList() : new ArrayList<>(dependencies);
    }

    public Class<?> getType() {
        return instance.getClass();
    }
}