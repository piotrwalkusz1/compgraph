package com.piotrwalkusz.compgraph.examples.subgraphs;

import com.piotrwalkusz.compgraph.Graph;
import com.piotrwalkusz.compgraph.examples.subgraphs.input.NetAmount;
import com.piotrwalkusz.compgraph.examples.subgraphs.input.TaxAmount;
import com.piotrwalkusz.compgraph.examples.subgraphs.node.GrossAmount;
import com.piotrwalkusz.compgraph.examples.subgraphs.node.VegetablesAndFruitCostAmount;
import com.piotrwalkusz.compgraph.examples.subgraphs.subgraph.FruitCost;
import com.piotrwalkusz.compgraph.examples.subgraphs.subgraph.VegetablesCost;

import java.math.BigDecimal;

public class App {

    public static void main(String[] args) {
        System.out.println(run());
    }

    public static String run() {
        final Graph graph = new Graph()
                .addSubgraph(VegetablesCost.class, subgraph -> {
                    subgraph.addInput(new NetAmount(BigDecimal.valueOf(100)));
                    subgraph.addInput(new TaxAmount(BigDecimal.valueOf(20)));
                })
                .addSubgraph(FruitCost.class, subgraph -> {
                    subgraph.addInput(new NetAmount(BigDecimal.valueOf(50)));
                    subgraph.addInput(new TaxAmount(BigDecimal.valueOf(30)));
                });

        return String.format(
                "Cost of vegetables and fruit = %s, Cost of vegetables = %s, Cost of fruit = %s",
                graph.evaluate(VegetablesAndFruitCostAmount.class),
                graph.getSubgraph(VegetablesCost.class).evaluate(GrossAmount.class),
                graph.getSubgraph(FruitCost.class).evaluate(GrossAmount.class)
        );
    }
}
