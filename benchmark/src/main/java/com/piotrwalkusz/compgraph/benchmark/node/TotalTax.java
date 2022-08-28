package com.piotrwalkusz.compgraph.benchmark.node;

import com.piotrwalkusz.compgraph.benchmark.subgraph.House;
import com.piotrwalkusz.compgraph.benchmark.subgraph.Vehicle;
import com.piotrwalkusz.compgraph.Node;

import javax.inject.Inject;
import java.math.BigDecimal;

public class TotalTax extends Node<BigDecimal> {

    @Inject
    private IncomeTax incomeTax;

    @Inject
    @House
    private PropertyTax houseTax;

    @Inject
    @Vehicle
    private PropertyTax vehicleTax;

    @Override
    protected BigDecimal evaluate() {
        return incomeTax.getValue().add(houseTax.getValue()).add(vehicleTax.getValue());
    }
}
