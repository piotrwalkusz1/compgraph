package com.piotrwalkusz.compgraph.examples.complex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void shouldCalculateIncomeTax() {
        assertEquals("Income tax = 135.9", App.run());
    }
}