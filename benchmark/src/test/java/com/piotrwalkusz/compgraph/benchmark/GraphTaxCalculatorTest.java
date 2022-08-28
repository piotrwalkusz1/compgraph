package com.piotrwalkusz.compgraph.benchmark;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphTaxCalculatorTest {

    @Test
    void shouldCalculateTotalTax() {
        assertEquals(37000, new GraphTaxCalculator().calculateTax(new TaxCalculationParams()).doubleValue());
    }
}