package com.piotrwalkusz.compgraph;

public abstract class Node<T> implements DisplayableValue {

    private T cachedValue;

    public Node() {
    }

    public Node(T cachedValue) {
        this.cachedValue = cachedValue;
    }

    public T getValue() {
        if (cachedValue == null) {
            cachedValue = evaluate();
        }
        return cachedValue;
    }

    protected abstract T evaluate();

    @Override
    public String getDisplayedValue() {
        return getValue().toString();
    }
}
