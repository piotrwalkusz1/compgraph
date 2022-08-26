package com.piotrwalkusz.compgraph.example.simple.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.example.simple.input.NetAmount;

import javax.inject.Inject;
import java.math.BigDecimal;

public class GrossAmount extends Node<BigDecimal> {

    @Inject
    private NetAmount netAmount;

    @Inject
    private GrossAmount grossAmount;

    @Override
    protected BigDecimal evaluate() {
        return netAmount.getValue().add(grossAmount.getValue());
    }
}
