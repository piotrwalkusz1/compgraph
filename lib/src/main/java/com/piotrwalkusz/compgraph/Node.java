package com.piotrwalkusz.compgraph;

public abstract class Node<T> {

    private boolean evaluated;
    private T cachedValue;

    public Node() {
        this.evaluated = false;
        this.cachedValue = null;
    }

    public Node(T cachedValue) {
        this.evaluated = true;
        this.cachedValue = cachedValue;
    }

    public T getValue() {
        if (!evaluated) {
            cachedValue = evaluate();
            evaluated = true;
        }
        return cachedValue;
    }

    protected abstract T evaluate();
}
