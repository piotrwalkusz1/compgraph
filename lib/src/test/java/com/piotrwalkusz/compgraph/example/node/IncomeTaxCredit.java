package com.piotrwalkusz.compgraph.example.node;

import com.piotrwalkusz.compgraph.Node;

import javax.inject.Inject;
import java.math.BigDecimal;

public class IncomeTaxCredit extends Node<BigDecimal> {

    @Inject
    private IncomeIncrease incomeIncrease;

    @Inject
    private MaxIncomeTaxCredit maxIncomeTaxCredit;

    @Override
    protected BigDecimal evaluate() {
        return incomeIncrease.getValue().min(maxIncomeTaxCredit.getValue()).max(BigDecimal.ZERO);
    }

    @Override
    public String getDisplayedValue() {
        return getValue().toString();
    }
}
