package com.piotrwalkusz.compgraph.injector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BeanContainer {

    private final List<Bean<?>> beans = new ArrayList<>();

    public void addBean(Bean<?> bean) {
        beans.add(bean);
    }

    public <T> Optional<Bean<T>> getBean(BeanMatcher<T> beanMatcher) {
        final List<Bean<T>> beans = getBeans(beanMatcher);
        if (beans.size() > 1) {
            throw new IllegalArgumentException("More than one bean found for matcher " + beanMatcher);
        }

        return beans.stream().findFirst();
    }

    public <T> List<Bean<T>> getBeans(BeanMatcher<T> beanMatcher) {
        return beans.stream()
                .filter(beanMatcher::match)
                .map(bean -> (Bean<T>) bean)
                .collect(Collectors.toList());
    }

    public List<Bean<?>> getBeans() {
        return new ArrayList<>(beans);
    }
}
