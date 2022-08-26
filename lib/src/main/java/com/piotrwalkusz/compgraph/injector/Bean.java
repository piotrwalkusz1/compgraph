package com.piotrwalkusz.compgraph.injector;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@AllArgsConstructor
public class Bean<T> {

    public Bean(T instance) {
        this(instance, new ArrayList<>());
    }

    T instance;
    List<Bean<?>> dependencies;
}
