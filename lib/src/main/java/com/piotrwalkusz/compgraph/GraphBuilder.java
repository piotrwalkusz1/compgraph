package com.piotrwalkusz.compgraph;

import com.google.inject.*;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public final class GraphBuilder {

    private final Injector parentInjector;

    public GraphBuilder() {
        this.parentInjector = null;
    }

    private GraphBuilder(Injector parentInjector) {
        this.parentInjector = parentInjector;
    }

    private static final class Input<T> {
        private final Key<T> key;
        private final T value;

        public Input(Key<T> key, T value) {
            this.key = key;
            this.value = value;
        }

        public Key<T> getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }
    }

    private final List<Input<?>> inputs = new ArrayList<>();
    private final List<Class<?>> nodes = new ArrayList<>();
    private final List<SubGraph> subGraphs = new ArrayList<>();

    public <T> GraphBuilder addInput(T value) {
        return addInput(value, (Class) value.getClass());
    }

    public <T> GraphBuilder addInput(T value, Class<T> type) {
        return addInput(value, Key.get(type));
    }

    public <T> GraphBuilder addInputWithAnnotation(T value, Class<? extends Annotation> annotationType) {
        return addInput(value, (Class) value.getClass(), annotationType);
    }

    public <T> GraphBuilder addInput(T value, Annotation annotation) {
        return addInput(value, (Class) value.getClass(), annotation);
    }

    public <T> GraphBuilder addInput(T value, Class<T> type, Class<? extends Annotation> annotationType) {
        return addInput(value, Key.get(type, annotationType));
    }

    public <T> GraphBuilder addInput(T value, Class<T> type, Annotation annotation) {
        return addInput(value, Key.get(type, annotation));
    }

    private <T> GraphBuilder addInput(T value, Key<T> key) {
        inputs.add(new Input<>(key, value));
        return this;
    }

    public GraphBuilder addNode(Class<? extends Node<?>> node) {
        nodes.add(node);
        return this;
    }

    public GraphBuilder addSubGraph(SubGraph subGraph) {
        subGraphs.add(subGraph);
        return this;
    }

    public Graph build() {
        final List<Class<?>> inputsClasses = new ArrayList<>();
        inputs.stream().map(Input::getValue).map(Object::getClass).forEach(inputsClasses::add);
        subGraphs.stream().map(Object::getClass).forEach(inputsClasses::add);

        final Module module = binder -> {
            inputs.forEach(input -> registerInputInBinder(binder, input));
            nodes.forEach(node -> registerNodeInBinder(binder, node));
            subGraphs.forEach(subGraph -> registerSubGraphInBinder(binder, subGraph));
            binder.requireExplicitBindings();
//            if (parentInjector == null) {
//                allowJITOnlyForNodes(binder, inputsClasses);
//            }
        };
        final Injector injector = parentInjector == null
                ? Guice.createInjector(module)
                : parentInjector.createChildInjector(module);

        subGraphs.forEach(subGraph -> {
            final GraphBuilder subGraphBuilder = new GraphBuilder(injector);
            subGraph.configure(subGraphBuilder);
            subGraphBuilder.inputs.stream().map(Input::getValue).map(Object::getClass).forEach(inputsClasses::add);
            inputsClasses.add(subGraph.getClass());
            subGraph.setGraph(subGraphBuilder.build());
        });

        return new Graph(injector);
    }

    private <T> void registerInputInBinder(Binder binder, Input<T> input) {
        binder.bind(input.getKey()).toInstance(input.getValue());
    }

    private void registerNodeInBinder(Binder binder, Class<?> node) {
        binder.bind(node).in(Singleton.class);
    }

    private void registerSubGraphInBinder(Binder binder, SubGraph subGraph) {
        binder.bind((Class) subGraph.getClass()).toInstance(subGraph);
    }

    private void allowJITOnlyForNodes(Binder binder, List<Class<?>> explicitInputs) {
        binder.bindListener(
                new AbstractMatcher<TypeLiteral<?>>() {
                    @Override
                    public boolean matches(TypeLiteral<?> typeLiteral) {
                        return !Node.class.isAssignableFrom(typeLiteral.getRawType()) && !explicitInputs.contains(typeLiteral.getRawType());
                    }
                },
                new TypeListener() {
                    @Override
                    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                        encounter.addError("JIT is only allowed for subclasses of Node. Class: " + type.getRawType());
                    }
                }
        );
    }
}
