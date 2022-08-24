package com.piotrwalkusz.compgraph.example.graph;

import com.piotrwalkusz.compgraph.Graph;
import com.piotrwalkusz.compgraph.SubGraph;
import com.piotrwalkusz.compgraph.example.input.Year;
import com.piotrwalkusz.compgraph.example.node.AdditionalHouseTax;
import com.piotrwalkusz.compgraph.example.node.AdditionalIncomeTax;
import com.piotrwalkusz.compgraph.example.node.AdditionalTaxRate;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public abstract class AdditionalTaxes extends SubGraph {

    private final int year;

    public BigDecimal getAdditionalHouseTax() {
        return getGraph().evaluate(AdditionalHouseTax.class);
    }

    public BigDecimal getAdditionalIncomeTax() {
        return getGraph().evaluate(AdditionalIncomeTax.class);
    }

    public BigDecimal getAdditionalTaxRate() {
        return getGraph().evaluate(AdditionalTaxRate.class);
    }

    @Override
    protected void setupGraph(Graph graph) {
        graph.addInput(new Year(year));
    }
}
