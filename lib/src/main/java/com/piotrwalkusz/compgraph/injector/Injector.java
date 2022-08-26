package com.piotrwalkusz.compgraph.injector;

import lombok.AccessLevel;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Injector {

    private final Injector parentInjector;
    private final List<Binding<?>> bindings;
    @Setter(AccessLevel.PROTECTED)
    private BeanFactory beanFactory;

    public Injector() {
        this(null);
    }

    public Injector(Injector parentInjector) {
        this.parentInjector = parentInjector;
        this.bindings = new ArrayList<>();
        this.beanFactory = new BeanFactory(this);
    }

    public final void bind(Object instance) {
        bind(instance, null);
    }

    @SuppressWarnings({"unchecked"})
    public final <T> void bind(T instance, Class<? extends Annotation> annotationType) {
        bind((Class<T>) instance.getClass(), annotationType).toInstance(instance);
    }

    public final <T> Binder<T> bind(Class<T> type) {
        return new Binder<>(Key.of(type), this);
    }

    public final <T> Binder<T> bind(Class<T> type, Class<? extends Annotation> annotationType) {
        return new Binder<>(Key.of(type, annotationType), this);
    }

    final void addBinding(Binding<?> binding) {
        bindings.add(binding);
    }

    public final <T> T getInstance(Class<T> type) {
        return getInstance(type, null);
    }

    public final <T> T getInstance(Class<T> type, Class<? extends Annotation> annotationType) {
        return getInstance(KeyMatcher.of(type, annotationType));
    }

    public final <T> T getInstance(KeyMatcher<T> keyMatcher) {
        return getBean(keyMatcher).getInstance();
    }

    public final <T> Bean<? extends T> getBean(KeyMatcher<T> keyMatcher) {
        return getProvider(keyMatcher).get();
    }

    public final List<Bean<?>> getExistingBeans() {
        return getExistingBindings().stream()
                .map(Binding::getProvider)
                .map(BeanProvider::getExistingBean)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public final <T> BeanProvider<? extends T> getProvider(KeyMatcher<T> keyMatcher) {
        return getBinding(keyMatcher).getProvider();
    }

    public final <T> Binding<? extends T> getBinding(KeyMatcher<T> keyMatcher) {
        return getExistingBindingFromThisOrParentInjector(keyMatcher)
                .orElseGet(() -> createJustInTimeBinding(keyMatcher));
    }

    public final <T> Optional<Binding<? extends T>> getExistingBindingFromThisOrParentInjector(KeyMatcher<T> keyMatcher) {
        return getExistingBindingFromParentInjector(keyMatcher)
                .or(() -> getExistingBinding(keyMatcher));
    }

    public final <T> Optional<Binding<? extends T>> getExistingBindingFromParentInjector(KeyMatcher<T> keyMatcher) {
        if (parentInjector == null) {
            return Optional.empty();
        }
        return parentInjector.getExistingBinding(keyMatcher);
    }

    public final <T> Optional<Binding<? extends T>> getExistingBinding(KeyMatcher<T> keyMatcher) {
        final List<Binding<? extends T>> bindings = getExistingBindings(keyMatcher);
        if (bindings.size() > 1) {
            throw new IllegalArgumentException("Found more than one binding for key matcher " + keyMatcher);
        }
        return bindings.stream().findFirst();
    }

    public final List<Binding<?>> getExistingBindings() {
        return getExistingBindings(null);
    }

    public final <T> List<Binding<? extends T>> getExistingBindings(KeyMatcher<T> keyMatcher) {
        return bindings.stream()
                .filter(binding -> keyMatcher == null || keyMatcher.match(binding.getKey()))
                .map(binding -> (Binding<? extends T>) binding)
                .collect(Collectors.toList());
    }

    public final <T> Bean<T> createNewBean(Class<T> type) {
        return beanFactory.createBean(type);
    }

    protected <T> Binding<T> createJustInTimeBinding(KeyMatcher<T> keyMatcher) {
        if (parentInjector != null) {
            try {
                return parentInjector.createJustInTimeBinding(keyMatcher);
            } catch (Exception exception) {
                // TODO: Add specific exception instead of "Exception"
            }
        }

        final CreatingProvider<T> provider = new CreatingProvider<>(keyMatcher.getType(), this);
        // Invoke Provider.get() to check if instance can be created successfully
        provider.get();
        final Binding<T> binding = new Binding<T>(keyMatcher.getKeyThatSatisfyMatcher(), provider);
        bindings.add(binding);

        return binding;
    }
}
