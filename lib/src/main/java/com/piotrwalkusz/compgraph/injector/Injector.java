package com.piotrwalkusz.compgraph.injector;

import lombok.AccessLevel;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Injector {

    private final Injector parentInjector;
    private final BindingContainer bindingContainer;
    @Setter(AccessLevel.PROTECTED)
    private BeanFactory beanFactory;

    public Injector() {
        this(null);
    }

    public Injector(Injector parentInjector) {
        this.parentInjector = parentInjector;
        this.bindingContainer = new BindingContainer();
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
        return bindingContainer.getBinding(keyMatcher);
    }

    public final List<Binding<?>> getExistingBindings() {
        return bindingContainer.getBindings();
    }

    public final <T> List<Binding<? extends T>> getExistingBindings(KeyMatcher<T> keyMatcher) {
        return bindingContainer.getBindings(keyMatcher);
    }

    final void addBinding(Binding<?> binding) {
        bindingContainer.addBinding(binding);
    }

    public final <T> Bean<T> createNewBean(Class<T> type) {
        return beanFactory.createBean(type);
    }

    protected <T> Binding<T> createJustInTimeBinding(KeyMatcher<T> keyMatcher) {
        if (parentInjector != null) {
            try {
                return parentInjector.createJustInTimeBinding(keyMatcher);
            } catch (Exception exception) {
                // The parent injector failed to create a binding
            }
        }

        final CreatingProvider<T> provider = new CreatingProvider<>(keyMatcher.getType(), this);
        // Invoke Provider.get() to check if an instance can be created successfully
        provider.get();
        final Binding<T> binding = new Binding<T>(keyMatcher.getKeyThatSatisfyMatcher(), provider);
        bindingContainer.addBinding(binding);

        return binding;
    }
}
