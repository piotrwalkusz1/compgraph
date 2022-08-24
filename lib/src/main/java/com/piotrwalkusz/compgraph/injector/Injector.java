package com.piotrwalkusz.compgraph.injector;

import com.piotrwalkusz.compgraph.utils.ReflectionUtils;
import lombok.SneakyThrows;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Injector {

    private static final List<Class<? extends Annotation>> SUPPORTED_INJECT_ANNOTATIONS = List.of(Inject.class);
    private static final List<Class<? extends Annotation>> SUPPORTED_QUALIFIER_ANNOTATIONS = List.of(Qualifier.class);

    private final Injector parentInjector;
    private final BeanContainer beanContainer;

    public Injector() {
        this(null);
    }

    public Injector(Injector parentInjector) {
        this.parentInjector = parentInjector;
        this.beanContainer = new BeanContainer();
    }

    public void addInstance(Object instance) {
        addInstance(instance, null);
    }

    public void addInstance(Object instance, Class<? extends Annotation> annotationType) {
        beanContainer.addBean(new Bean<>(instance, annotationType));
    }

    public <T> T getInstance(Class<T> type) {
        return getInstance(new BeanMatcher<>(type));
    }

    public <T> T getInstance(BeanMatcher<T> beanMatcher) {
        final Optional<T> existingInstance = getExistingInstance(beanMatcher);
        if (existingInstance.isPresent()) {
            return existingInstance.get();
        }

        if (beanMatcher.getAnnotationType() != null) {
            throw new IllegalArgumentException(String.format("No bean found for matcher %s. Bean with annotation cannot be created automatically.", beanMatcher));
        }

        final T createdInstance = createInstance(beanMatcher.getType());
        beanContainer.addBean(new Bean<>(createdInstance));

        return createdInstance;
    }

    public <T> Optional<T> getExistingInstance(BeanMatcher<T> beanMatcher) {
        for (Injector container = this; container != null; container = container.parentInjector) {
            final Optional<T> instance = container.beanContainer.getInstance(beanMatcher);
            if (instance.isPresent()) {
                return instance;
            }
        }

        return Optional.empty();
    }

    @SneakyThrows
    protected <T> T createInstance(Class<T> type) {
        final Constructor<T> constructor = getPublicNoArgsConstructor(type);
        final T instance = constructor.newInstance();
        injectToFields(type, instance);

        return instance;
    }

    private <T> void injectToFields(Class<T> type, T instance) {
        for (Field field : getInjectableFields(type)) {
            injectToField(instance, field);
        }
    }

    private List<Field> getInjectableFields(Class<?> type) {
        return ReflectionUtils.getFieldsFromClassAndSubclasses(type).stream()
                .filter(field -> ReflectionUtils.hasAnnotation(field, SUPPORTED_INJECT_ANNOTATIONS))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private void injectToField(Object object, Field field) {
        final BeanMatcher<?> beanMatcher = getBeanMatcher(field);
        final Object instance = getInstance(beanMatcher);
        field.setAccessible(true);
        field.set(object, instance);
    }

    private BeanMatcher<?> getBeanMatcher(Field field) {
        final Optional<Annotation> qualifierAnnotation = getQualifierAnnotation(field);
        return qualifierAnnotation
                .map(annotation -> new BeanMatcher(field.getType(), annotation.annotationType()))
                .orElseGet(() -> new BeanMatcher<>(field.getType()));
    }

    private Optional<Annotation> getQualifierAnnotation(Field field) {
        final List<Annotation> qualifierAnnotations = ReflectionUtils.getAnnotationsWithAnyMetaAnnotation(field, SUPPORTED_QUALIFIER_ANNOTATIONS);
        if (qualifierAnnotations.size() > 1) {
            throw new IllegalArgumentException("Detected more than one qualifier annotation");
        }

        return qualifierAnnotations.stream()
                .findFirst();
    }

    private <T> Constructor<T> getPublicNoArgsConstructor(Class<T> type) {
        return ReflectionUtils.getPublicNoArgsConstructor(type)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Type %s doesn't have public no args constructor", type.getCanonicalName())));
    }
}
