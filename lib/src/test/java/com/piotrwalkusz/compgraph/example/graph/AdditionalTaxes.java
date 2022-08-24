package com.piotrwalkusz.compgraph.example.graph;

import com.piotrwalkusz.compgraph.GraphBuilder;
import com.piotrwalkusz.compgraph.SubGraph;
import com.piotrwalkusz.compgraph.example.input.Year;
import com.piotrwalkusz.compgraph.example.node.AdditionalHouseTax;
import com.piotrwalkusz.compgraph.example.node.AdditionalIncomeTax;
import com.piotrwalkusz.compgraph.example.node.AdditionalTaxRate;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class AdditionalTaxes extends SubGraph {

    private final int year;

    @Override
    public void configure(GraphBuilder graphBuilder) {
        graphBuilder
                .addInput(new Year(year))
                .addNode(AdditionalHouseTax.class)
                .addNode(AdditionalIncomeTax.class)
                .addNode(AdditionalTaxRate.class);
    }

    public BigDecimal getAdditionalHouseTax() {
        return getGraph().evaluate(AdditionalHouseTax.class);
    }

    public BigDecimal getAdditionalIncomeTax() {
        return getGraph().evaluate(AdditionalIncomeTax.class);
    }

    public BigDecimal getAdditionalTaxRate() {
        return getGraph().evaluate(AdditionalTaxRate.class);
    }
}
