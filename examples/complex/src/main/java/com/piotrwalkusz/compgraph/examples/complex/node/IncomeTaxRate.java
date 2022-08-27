package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.complex.input.CalculationDate;
import com.piotrwalkusz.compgraph.examples.complex.service.TaxService;

import javax.inject.Inject;
import java.math.BigDecimal;

public class IncomeTaxRate extends Node<BigDecimal> {

    @Inject
    private TaxService taxService;

    @Inject
    private CalculationDate calculationDate;

    @Override
    protected BigDecimal evaluate() {
        return taxService.getIncomeTaxRateService(calculationDate.getValue());
    }
}
