package com.piotrwalkusz.compgraph.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {

    public static List<Field> getFieldsFromClassAndSubclasses(Class<?> type) {
        final List<Field> result = new ArrayList<>();
        Collections.addAll(result, type.getDeclaredFields());
        final Class<?> superclass = type.getSuperclass();
        if (superclass != null) {
            result.addAll(getFieldsFromClassAndSubclasses(superclass));
        }

        return result;
    }

    public static <T> Optional<Constructor<T>> getPublicNoArgsConstructor(Class<T> type) {
        return Arrays.stream(type.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0)
                .map(constructor -> (Constructor<T>) constructor)
                .findFirst();
    }

    public static List<Annotation> getAnnotationsWithAnyMetaAnnotation(Field field, List<Class<? extends Annotation>> metaAnnotations) {
        return Arrays.stream(field.getDeclaredAnnotations())
                .filter(annotation -> hasAnnotation(annotation.annotationType(), metaAnnotations))
                .collect(Collectors.toList());
    }

    public static boolean hasAnnotation(AccessibleObject accessibleObject, List<Class<? extends Annotation>> annotationsTypes) {
        return annotationsTypes.stream()
                .anyMatch(annotationType -> accessibleObject.getAnnotation(annotationType) != null);
    }

    public static boolean hasAnnotation(Class<?> type, List<Class<? extends Annotation>> annotationsTypes) {
        return annotationsTypes.stream()
                .anyMatch(annotationType -> type.getAnnotation(annotationType) != null);
    }
}
