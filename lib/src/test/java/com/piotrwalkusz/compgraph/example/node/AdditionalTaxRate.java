package com.piotrwalkusz.compgraph.example.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.example.input.Year;

import javax.inject.Inject;
import java.math.BigDecimal;

public class AdditionalTaxRate extends Node<BigDecimal> {

    @Inject
    private Year year;

    @Override
    protected BigDecimal evaluate() {
        return BigDecimal.valueOf(year.getValue() - 2000).max(BigDecimal.ZERO);
    }
}
