package com.piotrwalkusz.compgraph.benchmark;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardTaxCalculatorTest {

    @Test
    void shouldCalculateTotalTax() {
        assertEquals(37000, new StandardTaxCalculator().calculateTax(new TaxCalculationParams()).doubleValue());
    }
}