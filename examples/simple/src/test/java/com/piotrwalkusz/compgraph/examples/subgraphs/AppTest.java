package com.piotrwalkusz.compgraph.examples.subgraphs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void shouldCalculateGrossAmount() {
        assertEquals("Net amount = 100, Tax amount = 20, Gross amount = 120", App.run());
    }
}