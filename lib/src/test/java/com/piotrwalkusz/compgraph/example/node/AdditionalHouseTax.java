package com.piotrwalkusz.compgraph.example.node;

import com.piotrwalkusz.compgraph.Node;

import javax.inject.Inject;
import java.math.BigDecimal;

public class AdditionalHouseTax extends Node<BigDecimal> {

    @Inject
    private AdditionalTaxRate additionalTaxRate;

    @Override
    protected BigDecimal evaluate() {
        return additionalTaxRate.getValue().multiply(BigDecimal.valueOf(0.5));
    }
}
