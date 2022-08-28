package com.piotrwalkusz.compgraph.examples.complex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void shouldCalculateTotalTax() {
        assertEquals("Total tax = 37000.00", App.run());
    }
}