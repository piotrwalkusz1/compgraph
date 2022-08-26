package com.piotrwalkusz.compgraph.injector;

import com.piotrwalkusz.compgraph.injector.provider.ConstantProvider;
import com.piotrwalkusz.compgraph.injector.provider.CreatingProvider;
import com.piotrwalkusz.compgraph.injector.provider.LinkedProvider;
import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;

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

    public void to(Class<? extends T> type) {
        injector.addBinding(new Binding<>(key, new LinkedProvider<>(KeyMatcher.of(type), injector)));
    }

    public void to(Class<? extends T> type, Class<? extends Annotation> annotationType) {
        injector.addBinding(new Binding<>(key, new LinkedProvider<>(KeyMatcher.of(type, annotationType), injector)));
    }
}
