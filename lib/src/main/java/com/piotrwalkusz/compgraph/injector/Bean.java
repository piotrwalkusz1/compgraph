package com.piotrwalkusz.compgraph.injector;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public final class Bean<T> {

    public Bean(T instance) {
        this(instance, null);
    }

    public Bean(T instance, List<Bean<?>> dependencies) {
        this.instance = instance;
        this.dependencies = dependencies == null ? Collections.emptyList() : List.copyOf(dependencies);
    }

    private final T instance;
    private final List<Bean<?>> dependencies;
}
