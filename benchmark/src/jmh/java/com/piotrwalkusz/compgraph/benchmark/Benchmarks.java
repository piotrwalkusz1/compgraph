package com.piotrwalkusz.compgraph.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.math.BigDecimal;

public class Benchmarks {

    @State(Scope.Benchmark)
    public static class BenchmarkParams {
        private final TaxCalculationParams taxCalculationParams = new TaxCalculationParams();

        public TaxCalculationParams getTaxCalculationParams() {
            return taxCalculationParams;
        }
    }

    @Fork(value = 1, warmups = 2)
    @Benchmark
    public BigDecimal standard(BenchmarkParams params) {
        return new StandardTaxCalculator().calculateTax(params.getTaxCalculationParams());
    }

    @Fork(value = 1, warmups = 2)
    @Benchmark
    public BigDecimal graph(BenchmarkParams params) {
        return new GraphTaxCalculator().calculateTax(params.getTaxCalculationParams());
    }
}
