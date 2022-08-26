package com.piotrwalkusz.compgraph.injector;

import java.util.Optional;

public interface BeanProvider<T> {

    Bean<? extends T> get();

    Optional<Bean<? extends T>> getExistingBean();
}
