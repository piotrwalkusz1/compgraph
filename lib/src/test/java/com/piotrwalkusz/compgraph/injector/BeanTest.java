package com.piotrwalkusz.compgraph.injector;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanTest {

    @Test
    void shouldNotAllowToChangeDependenciesAfterBeanIsCreated() {
        final List<Bean<?>> dependencies = new ArrayList<>();
        dependencies.add(new Bean<>("D1"));
        dependencies.add(new Bean<>("D2"));
        final Bean<?> bean = new Bean<>("B", dependencies);

        assertThrows(UnsupportedOperationException.class, () -> bean.getDependencies().add(new Bean<>("D3")));
        dependencies.add(new Bean<>("D4"));

        assertEquals(2, bean.getDependencies().size());
        assertEquals("D1", bean.getDependencies().get(0).getInstance());
        assertEquals("D2", bean.getDependencies().get(1).getInstance());
    }
}