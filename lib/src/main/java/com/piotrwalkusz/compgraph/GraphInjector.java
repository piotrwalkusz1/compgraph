package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.injector.Binding;
import com.piotrwalkusz.compgraph.injector.Injector;
import com.piotrwalkusz.compgraph.injector.KeyMatcher;
import lombok.Getter;

public class GraphInjector extends Injector {

    @Getter
    private final SubgraphContainer subgraphContainer = new SubgraphContainer();

    public GraphInjector() {
        this(null);
    }

    public GraphInjector(GraphInjector parentInjector) {
        super(parentInjector);
        setBeanFactory(new GraphBeanFactory(this));
    }

    @Override
    protected <T> Binding<T> createJustInTimeBinding(KeyMatcher<T> keyMatcher) {
        if (Node.class.isAssignableFrom(keyMatcher.getType()) && keyMatcher.getAnnotationType() == null) {
            return super.createJustInTimeBinding(keyMatcher);
        }
        throw GraphException.justInTimeBindingNotSupportedForClass(keyMatcher.getType());
    }
}
