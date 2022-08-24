package com.piotrwalkusz.compgraph.injector;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Objects;

@Getter
public class BeanMatcher<T> {

    private final Class<T> type;
    private final Class<? extends Annotation> annotationType;

    public BeanMatcher(Class<T> type) {
        this(type, null);
    }

    public BeanMatcher(Class<T> type, Class<? extends Annotation> annotationType) {
        this.type = type;
        this.annotationType = annotationType;
    }

    public boolean match(Bean<?> bean) {
        return type.isAssignableFrom(bean.getType()) && Objects.equals(annotationType, bean.getAnnotationType());
    }

    @Override
    public String toString() {
        return String.format("BeanMatcher{type=%s, annotationType=%s}", type, annotationType);
    }
}
