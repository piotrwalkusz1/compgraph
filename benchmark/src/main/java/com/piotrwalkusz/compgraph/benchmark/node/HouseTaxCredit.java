package com.piotrwalkusz.compgraph.benchmark.node;

import com.piotrwalkusz.compgraph.benchmark.input.ChildrenCount;
import com.piotrwalkusz.compgraph.benchmark.input.Income;

import javax.inject.Inject;
import java.math.BigDecimal;

public class HouseTaxCredit extends TaxCredit {

    @Inject
    private Income income;

    @Inject
    private ChildrenCount childrenCount;

    @Override
    protected BigDecimal evaluate() {
        return BigDecimal.valueOf(100000)
                .subtract(income.getValue())
                .max(BigDecimal.ZERO)
                .add(BigDecimal.valueOf(10000).multiply(BigDecimal.valueOf(childrenCount.getValue())));
    }
}
