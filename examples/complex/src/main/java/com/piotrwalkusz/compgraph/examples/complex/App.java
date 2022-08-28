package com.piotrwalkusz.compgraph.examples.complex;

import com.piotrwalkusz.compgraph.Graph;
import com.piotrwalkusz.compgraph.examples.complex.input.*;
import com.piotrwalkusz.compgraph.examples.complex.node.HouseTaxCredit;
import com.piotrwalkusz.compgraph.examples.complex.node.TotalTax;
import com.piotrwalkusz.compgraph.examples.complex.node.VehicleTaxCredit;
import com.piotrwalkusz.compgraph.examples.complex.qualifier.PreviousYear;
import com.piotrwalkusz.compgraph.examples.complex.service.BillService;
import com.piotrwalkusz.compgraph.examples.complex.service.TaxService;
import com.piotrwalkusz.compgraph.examples.complex.subgraph.House;
import com.piotrwalkusz.compgraph.examples.complex.subgraph.Vehicle;

import java.math.BigDecimal;

public class App {

    public static void main(String[] args) {
        System.out.println(run());
    }

    public static String run() {
        final Graph graph = new Graph()
                .addInput(new BillService())
                .addInput(new TaxService())
                .addInput(new ChildrenCount(2))
                .addInput(new Income(BigDecimal.valueOf(100000)))
                .addInput(new Income(BigDecimal.valueOf(110000)), PreviousYear.class)
                .addInput(new TaxPayerId(2))
                .addInput(new Year(2022))
                .addSubgraph(House.class, subgraph -> subgraph
                        .addInput(new PropertyValue(BigDecimal.valueOf(700000)))
                        .addNode(HouseTaxCredit.class)
                )
                .addSubgraph(Vehicle.class, subgraph -> subgraph
                        .addInput(new PropertyValue(BigDecimal.valueOf(900000)))
                        .addNode(VehicleTaxCredit.class)
                );

        return String.format("Total tax = %s", graph.evaluate(TotalTax.class));
    }
}
