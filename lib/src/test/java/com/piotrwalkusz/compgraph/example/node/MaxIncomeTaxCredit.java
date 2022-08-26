package com.piotrwalkusz.compgraph.example.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.example.input.CalculationDate;
import com.piotrwalkusz.compgraph.example.service.TaxService;

import javax.inject.Inject;
import java.math.BigDecimal;

public class MaxIncomeTaxCredit extends Node<BigDecimal> {

    @Inject
    private TaxService taxService;

    @Inject
    private CalculationDate calculationDate;

    @Override
    protected BigDecimal evaluate() {
        return taxService.getMaxIncomeTaxCredit(calculationDate.getValue());
    }
}
