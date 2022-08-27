package com.piotrwalkusz.compgraph.injector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class BindingContainer {

    private final List<Binding<?>> bindings = new ArrayList<>();

    public final <T> Optional<Binding<? extends T>> getBinding(KeyMatcher<T> keyMatcher) {
        final List<Binding<? extends T>> bindings = getBindings(keyMatcher);
        if (bindings.size() > 1) {
            throw InjectorException.foundMoreThanOneBinding(keyMatcher, bindings);
        }
        return bindings.stream().findFirst();
    }

    public final List<Binding<?>> getBindings() {
        return getBindings(null);
    }

    @SuppressWarnings("unchecked")
    public final <T> List<Binding<? extends T>> getBindings(KeyMatcher<T> keyMatcher) {
        return bindings.stream()
                .filter(binding -> keyMatcher == null || keyMatcher.match(binding.getKey()))
                .map(binding -> (Binding<? extends T>) binding)
                .collect(Collectors.toList());
    }

    public final void addBinding(Binding<?> binding) {
        bindings.add(binding);
    }
}
