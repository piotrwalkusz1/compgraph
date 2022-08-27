package com.piotrwalkusz.compgraph.injector;

import com.piotrwalkusz.compgraph.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BeanFactory {

    private static final List<Class<? extends Annotation>> SUPPORTED_INJECT_ANNOTATIONS = List.of(Inject.class);
    private static final List<Class<? extends Annotation>> SUPPORTED_QUALIFIER_ANNOTATIONS = List.of(Qualifier.class);

    protected final Injector injector;

    @SneakyThrows
    public final <T> Bean<T> createBean(Class<T> type) {
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
        final Bean<?> bean = getBeanToInjectToField(field);
        field.setAccessible(true);
        field.set(object, bean.getInstance());

        return bean;
    }

    protected Bean<?> getBeanToInjectToField(Field field) {
        final KeyMatcher<?> keyMatcher = getKeyMatcher(field);
        return injector.getBean(keyMatcher);
    }

    protected final KeyMatcher<?> getKeyMatcher(Field field) {
        final Optional<Annotation> qualifierAnnotation = getQualifierAnnotation(field);
        if (qualifierAnnotation.isPresent()) {
            return KeyMatcher.of(field.getType(), qualifierAnnotation.get().annotationType());
        }
        return KeyMatcher.of(field.getType());
    }

    private Optional<Annotation> getQualifierAnnotation(Field field) {
        final List<Annotation> qualifierAnnotations = ReflectionUtils.getAnnotationsWithAnyMetaAnnotation(field, SUPPORTED_QUALIFIER_ANNOTATIONS);
        if (qualifierAnnotations.size() > 1) {
            throw InjectorException.fieldHasMoreThanOneQualifier(field, qualifierAnnotations);
        }

        return qualifierAnnotations.stream()
                .findFirst();
    }

    private <T> Constructor<T> getPublicNoArgsConstructor(Class<T> type) {
        return ReflectionUtils.getPublicNoArgsConstructor(type)
                .orElseThrow(() -> InjectorException.noPublicNoArgsConstructor(type));
    }
}
