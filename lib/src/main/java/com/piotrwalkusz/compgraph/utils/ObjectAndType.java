package com.piotrwalkusz.compgraph.utils;

import lombok.Value;

@Value
public class ObjectAndType<T> {

    T object;
    Class<T> type;
}
