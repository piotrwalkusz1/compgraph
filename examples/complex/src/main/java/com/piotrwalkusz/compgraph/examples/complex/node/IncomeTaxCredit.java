package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.complex.input.ChildrenCount;

import javax.inject.Inject;
import java.math.BigDecimal;

public class IncomeTaxCredit extends Node<BigDecimal> {

    @Inject
    private IncomeDecreased incomeDecreased;

    @Inject
    private ChildrenCount childrenCount;

    @Override
    protected BigDecimal evaluate() {
        return childrenCount.getValue() > 0 && incomeDecreased.getValue() ? BigDecimal.valueOf(2000) : BigDecimal.ZERO;
    }
}
