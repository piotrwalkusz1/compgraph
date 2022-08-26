package com.piotrwalkusz.compgraph.injector;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Binder<T> {

    private final Key<T> key;
    private final Injector injector;

    public void toInstance(T instance) {
        injector.addBinding(new Binding<>(key, new ConstantProvider<>(new Bean<>(instance))));
    }

    public void toSelf() {
        injector.addBinding(new Binding<>(key, new CreatingProvider<>(key.getType(), injector)));
    }
}
