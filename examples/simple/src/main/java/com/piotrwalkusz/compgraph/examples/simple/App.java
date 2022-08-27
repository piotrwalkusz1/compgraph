package com.piotrwalkusz.compgraph.examples.simple;

import com.piotrwalkusz.compgraph.Graph;
import com.piotrwalkusz.compgraph.examples.simple.input.NetAmount;
import com.piotrwalkusz.compgraph.examples.simple.input.TaxAmount;
import com.piotrwalkusz.compgraph.examples.simple.node.GrossAmount;

import java.math.BigDecimal;

public class App {

    public static void main(String[] args) {
        System.out.println(run());
    }

    public static String run() {
        final Graph graph = new Graph()
                .addInput(new NetAmount(BigDecimal.valueOf(100)))
                .addInput(new TaxAmount(BigDecimal.valueOf(20)));

        return String.format(
                "Net amount = %s, Tax amount = %s, Gross amount = %s",
                graph.getInstance(NetAmount.class).getValue(),
                graph.getInstance(TaxAmount.class).getValue(),
                graph.evaluate(GrossAmount.class)
        );
    }
}
