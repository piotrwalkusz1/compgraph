package com.piotrwalkusz.compgraph.injector;

final class InjectorBeanCreatorContext implements BeanCreatorContext {

    private final Injector injector;

    InjectorBeanCreatorContext(Injector injector) {
        this.injector = injector;
    }

    @Override
    public <T> Bean<T> getBean(BeanMatcher<T> beanMatcher) {
        return injector.getBean(beanMatcher);
    }
}
