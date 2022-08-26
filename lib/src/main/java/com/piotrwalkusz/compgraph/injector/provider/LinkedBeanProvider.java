package com.piotrwalkusz.compgraph.injector.provider;

import com.piotrwalkusz.compgraph.injector.Bean;
import com.piotrwalkusz.compgraph.injector.BeanProvider;
import com.piotrwalkusz.compgraph.injector.Injector;
import com.piotrwalkusz.compgraph.injector.KeyMatcher;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class LinkedBeanProvider<T> implements BeanProvider<T> {

    private final KeyMatcher<T> keyMatcher;
    private final Injector injector;

    @Override
    public Bean<? extends T> get() {
        return injector.getBean(keyMatcher);
    }

    @Override
    public Optional<Bean<? extends T>> getExistingBean() {
        return Optional.empty();
    }
}
