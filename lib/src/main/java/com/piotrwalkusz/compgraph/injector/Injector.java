package com.piotrwalkusz.compgraph.injector;

import com.piotrwalkusz.compgraph.injector.provider.RegisteredTypeBeanProvider;
import com.piotrwalkusz.compgraph.utils.ClassUtils;
import com.piotrwalkusz.compgraph.utils.ObjectAndType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Injector {

    private final Injector parentInjector;
    private final BeanFactory beanFactory;
    private final List<Binding<?>> bindings;

    public Injector() {
        this(null);
    }

    public Injector(Injector parentInjector) {
        this.parentInjector = parentInjector;
        this.beanFactory = new BeanFactory(this);
        this.bindings = new ArrayList<>();
    }

    public <T> void bind(Object instance) {
        final ObjectAndType<T> objectAndType = ClassUtils.getObjectAndType(instance);
        bind(objectAndType.getType()).toInstance(objectAndType.getObject());
    }

    public <T> void bind(T instance, Class<? extends Annotation> annotationType) {
        final ObjectAndType<T> objectAndType = ClassUtils.getObjectAndType(instance);
        bind(objectAndType.getType(), annotationType).toInstance(instance);
    }

    public <T> Binder<T> bind(Class<T> type) {
        return new Binder<>(new Key<>(type), this);
    }

    public <T> Binder<T> bind(Class<T> type, Class<? extends Annotation> annotationType) {
        return new Binder<>(new Key<>(type, annotationType), this);
    }

    void addBinding(Binding<?> binding) {
        bindings.add(binding);
    }

    public <T> T getInstance(Class<T> type) {
        return getInstance(new KeyMatcher<>(type));
    }

    public <T> T getInstance(KeyMatcher<T> keyMatcher) {
        return getBean(keyMatcher).getInstance();
    }

    public <T> Bean<? extends T> getBean(KeyMatcher<T> keyMatcher) {
        return getProvider(keyMatcher).get();
    }

    public List<Bean<?>> getExistingBeans() {
        return getExistingBindings().stream()
                .map(Binding::getProvider)
                .map(BeanProvider::getExistingBean)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public <T> BeanProvider<? extends T> getProvider(KeyMatcher<T> keyMatcher) {
        return getBinding(keyMatcher).getProvider();
    }

    public <T> Binding<? extends T> getBinding(KeyMatcher<T> keyMatcher) {
        return getExistingBindingFromThisOrParentInjector(keyMatcher)
                .orElseGet(() -> createJustInTimeBinding(keyMatcher));
    }

    public <T> Optional<Binding<? extends T>> getExistingBindingFromThisOrParentInjector(KeyMatcher<T> keyMatcher) {
        return getExistingBindingFromParentInjector(keyMatcher)
                .or(() -> getExistingBinding(keyMatcher));
    }

    public <T> Optional<Binding<? extends T>> getExistingBindingFromParentInjector(KeyMatcher<T> keyMatcher) {
        if (parentInjector == null) {
            return Optional.empty();
        }
        return parentInjector.getExistingBinding(keyMatcher);
    }

    public <T> Optional<Binding<? extends T>> getExistingBinding(KeyMatcher<T> keyMatcher) {
        final List<Binding<? extends T>> bindings = getExistingBindings(keyMatcher);
        if (bindings.size() > 1) {
            throw new IllegalArgumentException("Found more than one binding for key matcher " + keyMatcher);
        }
        return bindings.stream().findFirst();
    }

    public List<Binding<?>> getExistingBindings() {
        return getExistingBindings(null);
    }

    public <T> List<Binding<? extends T>> getExistingBindings(KeyMatcher<T> keyMatcher) {
        return bindings.stream()
                .filter(binding -> keyMatcher == null || keyMatcher.match(binding.getKey()))
                .map(binding -> (Binding<? extends T>) binding)
                .collect(Collectors.toList());
    }

    public <T> Bean<T> createNewBean(Class<T> type) {
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

        final RegisteredTypeBeanProvider<T> provider = new RegisteredTypeBeanProvider<>(keyMatcher.getType(), this);
        // Invoke Provider.get() to check if instance can be created successfully
        provider.get();
        final Binding<T> binding = new Binding<T>(keyMatcher.getKeyThatSatisfyMatcher(), provider);
        bindings.add(binding);

        return binding;
    }
}
