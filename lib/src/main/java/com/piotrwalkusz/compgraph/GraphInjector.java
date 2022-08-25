package com.piotrwalkusz.compgraph;

import com.piotrwalkusz.compgraph.injector.Bean;
import com.piotrwalkusz.compgraph.injector.Injector;

public class GraphInjector extends Injector {

    public GraphInjector() {
        super();
    }

    public GraphInjector(GraphInjector parentGraph) {
        super(parentGraph);
    }

    @Override
    protected <T> Bean<T> createBean(Class<T> type) {
        if (!Node.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format("Cannot create instance of type %s. Only instance of Node can be created.", type));
        }
        return super.createBean(type);
    }
}
