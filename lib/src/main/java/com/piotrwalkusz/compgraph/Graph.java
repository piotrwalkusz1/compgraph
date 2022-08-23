package com.piotrwalkusz.compgraph;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.grapher.ShortNameFactory;
import com.google.inject.grapher.graphviz.PortIdFactoryImpl;
import com.piotrwalkusz.compgraph.grapher.CustomGraphvizGrapher;

import java.io.IOException;
import java.io.PrintWriter;

public final class Graph {

    private final Injector injector;

    Graph(Injector injector) {
        this.injector = injector;
    }

    public <T> T evaluate(Class<? extends Node<T>> node) {
        if (injector.getExistingBinding(Key.get(node)) == null) {
            throw new IllegalArgumentException("Node doesn't exist in Graph");
        }

        return injector.getInstance(node).getValue();
    }

    public void drawGraph(String filename) throws IOException {
        final PrintWriter out = new PrintWriter(filename, "UTF-8");
        final CustomGraphvizGrapher grapher = new CustomGraphvizGrapher(new ShortNameFactory(), new PortIdFactoryImpl());
        grapher.setOut(out);
        grapher.graph(injector);
    }
}
