package com.piotrwalkusz.compgraph.examples.simple.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.simple.input.TaxAmount;
import com.piotrwalkusz.compgraph.examples.simple.input.NetAmount;

import javax.inject.Inject;
import java.math.BigDecimal;

public class GrossAmount extends Node<BigDecimal> {

    @Inject
    private NetAmount netAmount;

    @Inject
    private TaxAmount taxAmount;

    @Override
    protected BigDecimal evaluate() {
        return netAmount.getValue().add(taxAmount.getValue());
    }
}
