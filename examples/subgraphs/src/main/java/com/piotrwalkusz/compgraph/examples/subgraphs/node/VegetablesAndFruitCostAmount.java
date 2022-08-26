package com.piotrwalkusz.compgraph.examples.subgraphs.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.subgraphs.subgraph.FruitCost;
import com.piotrwalkusz.compgraph.examples.subgraphs.subgraph.VegetablesCost;

import javax.inject.Inject;
import java.math.BigDecimal;

public class VegetablesAndFruitCostAmount extends Node<BigDecimal> {

    @Inject
    @VegetablesCost
    private GrossAmount vegetablesCost;

    @Inject
    @FruitCost
    private GrossAmount fruitCost;

    @Override
    protected BigDecimal evaluate() {
        return vegetablesCost.getValue().add(fruitCost.getValue());
    }
}
