package com.piotrwalkusz.compgraph.injector;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public final class ConstantProvider<T> implements BeanProvider<T> {

    private final Bean<T> bean;

    @Override
    public Bean<? extends T> get() {
        return bean;
    }

    @Override
    public Optional<Bean<? extends T>> getExistingBean() {
        return Optional.of(bean);
    }
}
