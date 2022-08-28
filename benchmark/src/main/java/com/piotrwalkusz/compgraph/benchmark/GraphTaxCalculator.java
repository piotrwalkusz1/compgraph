package com.piotrwalkusz.compgraph.benchmark;

import com.piotrwalkusz.compgraph.Graph;
import com.piotrwalkusz.compgraph.benchmark.input.*;
import com.piotrwalkusz.compgraph.benchmark.node.HouseTaxCredit;
import com.piotrwalkusz.compgraph.benchmark.node.TotalTax;
import com.piotrwalkusz.compgraph.benchmark.node.VehicleTaxCredit;
import com.piotrwalkusz.compgraph.benchmark.qualifier.PreviousYear;
import com.piotrwalkusz.compgraph.benchmark.subgraph.House;
import com.piotrwalkusz.compgraph.benchmark.subgraph.Vehicle;

import java.math.BigDecimal;

public class GraphTaxCalculator {

    public BigDecimal calculateTax(TaxCalculationParams params) {
        return new Graph()
                .addInput(params.getBillService())
                .addInput(params.getTaxService())
                .addInput(new ChildrenCount(params.getChildrenCount()))
                .addInput(new Income(params.getIncomeInCurrentYear()))
                .addInput(new Income(params.getIncomeInPreviousYear()), PreviousYear.class)
                .addInput(new TaxPayerId(params.getTaxPayerId()))
                .addInput(new Year(params.getYear()))
                .addSubgraph(House.class, subgraph -> subgraph
                        .addInput(new PropertyValue(params.getHouseValue()))
                        .addNode(HouseTaxCredit.class)
                )
                .addSubgraph(Vehicle.class, subgraph -> subgraph
                        .addInput(new PropertyValue(params.getVehicleValue()))
                        .addNode(VehicleTaxCredit.class)
                )
                .evaluate(TotalTax.class);
    }
}
