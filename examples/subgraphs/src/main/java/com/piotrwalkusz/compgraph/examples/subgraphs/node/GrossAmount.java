package com.piotrwalkusz.compgraph.examples.subgraphs.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.subgraphs.input.NetAmount;
import com.piotrwalkusz.compgraph.examples.subgraphs.input.TaxAmount;

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
