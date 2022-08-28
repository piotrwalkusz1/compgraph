package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.examples.complex.input.ChildrenCount;
import com.piotrwalkusz.compgraph.examples.complex.input.Income;

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
