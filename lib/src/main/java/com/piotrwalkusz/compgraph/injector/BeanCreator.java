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

public class BeanCreator {

    private static final List<Class<? extends Annotation>> SUPPORTED_INJECT_ANNOTATIONS = List.of(Inject.class);
    private static final List<Class<? extends Annotation>> SUPPORTED_QUALIFIER_ANNOTATIONS = List.of(Qualifier.class);

    private final BeanCreatorContext context;

    public BeanCreator(BeanCreatorContext context) {
        this.context = context;
    }

    @SneakyThrows
    public <T> Bean<T> createBean(Class<T> type) {
        final Constructor<T> constructor = getPublicNoArgsConstructor(type);
        final T instance = constructor.newInstance();
        final List<Bean<?>> dependencies = injectToFields(type, instance);

        return new Bean<>(instance, dependencies);
    }

    private <T> List<Bean<?>> injectToFields(Class<T> type, T instance) {
        return getInjectableFields(type).stream()
                .map(field -> injectToField(instance, field))
                .collect(Collectors.toList());
    }

    private List<Field> getInjectableFields(Class<?> type) {
        return ReflectionUtils.getFieldsFromClassAndSubclasses(type).stream()
                .filter(field -> ReflectionUtils.hasAnnotation(field, SUPPORTED_INJECT_ANNOTATIONS))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Bean<?> injectToField(Object object, Field field) {
        final BeanMatcher<?> beanMatcher = getBeanMatcher(field);
        final Bean<?> bean = context.getBean(beanMatcher);
        field.setAccessible(true);
        field.set(object, bean.getInstance());

        return bean;
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
