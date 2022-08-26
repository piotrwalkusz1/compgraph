package com.piotrwalkusz.compgraph.injector;

import com.piotrwalkusz.compgraph.injector.provider.ConstantBeanProvider;
import com.piotrwalkusz.compgraph.injector.provider.LinkedBeanProvider;
import com.piotrwalkusz.compgraph.injector.provider.RegisteredTypeBeanProvider;
import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;

@AllArgsConstructor
public class Binder<T> {

    private final Key<T> key;
    private final Injector injector;

    public void toInstance(T instance) {
        injector.addBinding(new Binding<>(key, new ConstantBeanProvider<>(new Bean<>(instance))));
    }

    public void toSelf() {
        injector.addBinding(new Binding<>(key, new RegisteredTypeBeanProvider<>(key.getType(), injector)));
    }

    public void to(Class<? extends T> type) {
        injector.addBinding(new Binding<>(key, new LinkedBeanProvider<>(new KeyMatcher<>(type), injector)));
    }

    public void to(Class<? extends T> type, Class<? extends Annotation> annotationType) {
        injector.addBinding(new Binding<>(key, new LinkedBeanProvider<>(new KeyMatcher<>(type, annotationType), injector)));
    }
}
