package com.piotrwalkusz.compgraph.benchmark.node;

import com.piotrwalkusz.compgraph.benchmark.input.Year;
import com.piotrwalkusz.compgraph.benchmark.service.TaxService;
import com.piotrwalkusz.compgraph.Node;

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
