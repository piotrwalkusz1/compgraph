package com.piotrwalkusz.compgraph.injector;

import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class Injector {

    private final Injector parentInjector;
    private final BeanContainer beanContainer;
    private final BeanCreator beanCreator;

    public Injector() {
        this(null);
    }

    public Injector(Injector parentInjector) {
        this.parentInjector = parentInjector;
        this.beanContainer = new BeanContainer();
        this.beanCreator = new BeanCreator(new InjectorBeanCreatorContext(this));
    }

    public void addInstance(Object instance) {
        addInstance(instance, null);
    }

    public void addInstance(Object instance, Class<? extends Annotation> annotationType) {
        beanContainer.addBean(new Bean<>(instance, annotationType));
    }

    public <T> T getInstance(Class<T> type) {
        return getBean(type).getInstance();
    }

    public <T> Bean<T> getBean(Class<T> type) {
        return getBean(new BeanMatcher<>(type));
    }

    public <T> Bean<T> getBean(BeanMatcher<T> beanMatcher) {
        final Optional<Bean<T>> existingBean = getExistingBean(beanMatcher);
        if (existingBean.isPresent()) {
            return existingBean.get();
        }

        if (beanMatcher.getAnnotationType() != null) {
            throw new IllegalArgumentException(String.format("No bean found for matcher %s. Bean with annotation cannot be created automatically.", beanMatcher));
        }

        final Bean<T> bean = createBean(beanMatcher.getType());
        beanContainer.addBean(bean);

        return bean;
    }

    public <T> Optional<Bean<T>> getExistingBean(BeanMatcher<T> beanMatcher) {
        for (Injector container = this; container != null; container = container.parentInjector) {
            final Optional<Bean<T>> bean = container.beanContainer.getBean(beanMatcher);
            if (bean.isPresent()) {
                return bean;
            }
        }

        return Optional.empty();
    }

    public List<Bean<?>> getBeans() {
        return beanContainer.getBeans();
    }

    @SneakyThrows
    protected <T> Bean<T> createBean(Class<T> type) {
        return beanCreator.createBean(type);
    }
}
