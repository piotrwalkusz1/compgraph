package com.piotrwalkusz.compgraph.injector.provider;

import com.piotrwalkusz.compgraph.injector.Bean;
import com.piotrwalkusz.compgraph.injector.BeanProvider;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class ConstantBeanProvider<T> implements BeanProvider<T> {

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
