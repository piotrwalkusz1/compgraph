package com.piotrwalkusz.compgraph.benchmark.node;

import com.piotrwalkusz.compgraph.benchmark.input.Income;
import com.piotrwalkusz.compgraph.Node;

import javax.inject.Inject;
import java.math.BigDecimal;

public class IncomeTax extends Node<BigDecimal> {

    @Inject
    private Income income;

    @Inject
    private IncomeTaxCredit incomeTaxCredit;

    @Inject
    private AdditionalIncomeTax additionalIncomeTax;

    @Override
    protected BigDecimal evaluate() {
        return income.getValue()
                .multiply(BigDecimal.valueOf(0.2))
                .add(additionalIncomeTax.getValue())
                .subtract(incomeTaxCredit.getValue())
                .max(BigDecimal.ZERO);
    }
}
