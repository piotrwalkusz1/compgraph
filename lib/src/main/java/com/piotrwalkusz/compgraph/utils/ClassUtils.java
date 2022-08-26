package com.piotrwalkusz.compgraph.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassUtils {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T, R extends T> ObjectAndType<R> getObjectAndType(T object) {
        final Class type = object.getClass();
        return (ObjectAndType<R>) new ObjectAndType(object, type);
    }
}
