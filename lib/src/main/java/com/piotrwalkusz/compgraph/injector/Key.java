package com.piotrwalkusz.compgraph.injector;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.annotation.Annotation;

@Data
@AllArgsConstructor
public class Key<T> {

    public Key(Class<T> type) {
        this(type, null);
    }

    private final Class<T> type;
    private final Class<? extends Annotation> annotationType;
}
