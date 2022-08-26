package com.piotrwalkusz.compgraph.injector;

import lombok.Value;

@Value
public class Binding<T> {

    Key<T> key;
    BeanProvider<? extends T> provider;
}
