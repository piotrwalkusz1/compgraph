package com.piotrwalkusz.compgraph.injector;

public interface BeanCreatorContext {

    <T> Bean<T> getBean(BeanMatcher<T> beanMatcher);
}
