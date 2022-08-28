package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.complex.subgraph.House;
import com.piotrwalkusz.compgraph.examples.complex.subgraph.Vehicle;

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
