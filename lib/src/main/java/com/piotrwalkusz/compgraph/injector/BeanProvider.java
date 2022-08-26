package com.piotrwalkusz.compgraph.injector;

import com.piotrwalkusz.compgraph.injector.Bean;

import java.util.Optional;

public interface BeanProvider<T> {

    Bean<? extends T> get();

    Optional<Bean<? extends T>> getExistingBean();
}
