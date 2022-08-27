package com.piotrwalkusz.compgraph.injector;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public final class ConstantProvider<T> implements BeanProvider<T> {

    private final Bean<T> bean;

    @Override
    public final Bean<? extends T> get() {
        return bean;
    }

    @Override
    public final Optional<Bean<? extends T>> getExistingBean() {
        return Optional.of(bean);
    }

    @Override
    public final String toString() {
        return String.format("<instance of %s>", bean.getInstance().getClass().getCanonicalName());
    }
}
