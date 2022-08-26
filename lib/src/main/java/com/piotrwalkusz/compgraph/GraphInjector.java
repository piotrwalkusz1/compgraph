package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.injector.Binding;
import com.piotrwalkusz.compgraph.injector.Injector;
import com.piotrwalkusz.compgraph.injector.KeyMatcher;

public class GraphInjector extends Injector {

    public GraphInjector(Graph graph, GraphInjector parentGraph) {
        super(parentGraph, new GraphBeanFactory(graph));
    }

    @Override
    protected <T> Binding<T> createJustInTimeBinding(KeyMatcher<T> keyMatcher) {
        if (Node.class.isAssignableFrom(keyMatcher.getType()) && keyMatcher.getAnnotationType() == null) {
            return super.createJustInTimeBinding(keyMatcher);
        }
        throw new IllegalArgumentException(String.format("Cannot create instance of type %s. Only instance of Node can be created.", keyMatcher.getType()));
    }
}
