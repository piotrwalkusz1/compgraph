package com.piotrwalkusz.compgraph.injector;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Bean<T> {

    public Bean(T instance) {
        this(instance, null);
    }

    public Bean(T instance, List<Bean<?>> dependencies) {
        this.instance = instance;
        this.dependencies = dependencies == null ? new ArrayList<>() : new ArrayList<>(dependencies);
    }

    private final T instance;
    private final List<Bean<?>> dependencies;
}
