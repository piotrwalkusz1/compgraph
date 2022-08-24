package com.piotrwalkusz.compgraph.example.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.example.input.CalculationDate;

import javax.inject.Inject;
import java.math.BigDecimal;

public class AdditionalIncomeTax extends Node<BigDecimal> {

    @Inject
    private AdditionalTaxRate additionalTaxRate;

    @Inject
    private CalculationDate calculationDate;

    @Override
    protected BigDecimal evaluate() {
        return additionalTaxRate.getValue().multiply(BigDecimal.valueOf(calculationDate.getValue().getYear() > 2020 ? 0.3 : 0.2));
    }

    @Override
    public String getDisplayedValue() {
        return getValue().toString();
    }
}
