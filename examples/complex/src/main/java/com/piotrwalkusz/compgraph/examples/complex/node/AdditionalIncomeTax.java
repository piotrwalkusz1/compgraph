package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.complex.input.Year;
import com.piotrwalkusz.compgraph.examples.complex.service.TaxService;

import javax.inject.Inject;
import java.math.BigDecimal;

public class AdditionalIncomeTax extends Node<BigDecimal> {

    @Inject
    private TaxService taxService;

    @Inject
    private Year year;

    @Override
    protected BigDecimal evaluate() {
        return taxService.getAdditionalIncomeTaxByYear(year.getValue());
    }
}
