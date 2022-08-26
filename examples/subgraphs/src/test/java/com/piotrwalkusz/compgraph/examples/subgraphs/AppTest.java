package com.piotrwalkusz.compgraph.examples.subgraphs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void shouldCalculateVegetablesAndFruitCost() {
        assertEquals("Cost of vegetables and fruit = 200, Cost of vegetables = 120, Cost of fruit = 80", App.run());
    }
}