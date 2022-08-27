package com.piotrwalkusz.compgraph.injector;

import java.util.Optional;

public final class CreatingProvider<T> implements BeanProvider<T> {

    private final Class<T> type;
    private final Injector injector;
    private Bean<T> bean;

    public CreatingProvider(Class<T> type, Injector injector) {
        this.type = type;
        this.injector = injector;
    }

    @Override
    public final Bean<? extends T> get() {
        if (bean == null) {
            bean = injector.createNewBean(type);
        }
        return bean;
    }

    @Override
    public final Optional<Bean<? extends T>> getExistingBean() {
        return Optional.ofNullable(bean);
    }

    @Override
    public final String toString() {
        return String.format("<provider of %s>", type.getCanonicalName());
    }
}
