package com.piotrwalkusz.compgraph.injector.provider;

import com.piotrwalkusz.compgraph.injector.Bean;
import com.piotrwalkusz.compgraph.injector.BeanProvider;
import com.piotrwalkusz.compgraph.injector.Injector;

import java.util.Optional;

public class RegisteredTypeBeanProvider<T> implements BeanProvider<T> {

    private final Class<T> type;
    private final Injector injector;
    private Bean<T> bean;

    public RegisteredTypeBeanProvider(Class<T> type, Injector injector) {
        this.type = type;
        this.injector = injector;
    }

    @Override
    public Bean<? extends T> get() {
        if (bean == null) {
            bean = injector.createNewBean(type);
        }
        return bean;
    }

    @Override
    public Optional<Bean<? extends T>> getExistingBean() {
        return Optional.ofNullable(bean);
    }
}
