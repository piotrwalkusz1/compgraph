package com.piotrwalkusz.compgraph.examples.complex;

import com.piotrwalkusz.compgraph.Graph;
import com.piotrwalkusz.compgraph.examples.complex.input.CalculationDate;
import com.piotrwalkusz.compgraph.examples.complex.input.Income;
import com.piotrwalkusz.compgraph.examples.complex.input.TaxpayerId;
import com.piotrwalkusz.compgraph.examples.complex.input.Year;
import com.piotrwalkusz.compgraph.examples.complex.node.IncomeTax;
import com.piotrwalkusz.compgraph.examples.complex.qualifier.PreviousYear;
import com.piotrwalkusz.compgraph.examples.complex.service.BillServiceImpl;
import com.piotrwalkusz.compgraph.examples.complex.service.TaxService;
import com.piotrwalkusz.compgraph.examples.complex.subgraph.Year2021;
import com.piotrwalkusz.compgraph.examples.complex.subgraph.Year2022;

import java.math.BigDecimal;
import java.time.LocalDate;

public class App {

    public static void main(String[] args) {
        System.out.println(run());
    }

    public static String run() {
        final Graph graph = new Graph()
                .addInput(new CalculationDate(LocalDate.of(2022, 1, 1)))
                .addInput(new Income(BigDecimal.valueOf(30000)))
                .addInput(new TaxpayerId(12))
                .addInput(new Income(BigDecimal.valueOf(1223)), PreviousYear.class)
                .addInput(new IncomeTax(BigDecimal.valueOf(1200)), PreviousYear.class)
                .addInput(new TaxService())
                .addInput(new BillServiceImpl())
                .addSubgraph(Year2021.class, subgraph -> subgraph.addInput(new Year(2021)))
                .addSubgraph(Year2022.class, subgraph -> subgraph.addInput(new Year(2022)));

        return String.format("Income tax = %s", graph.evaluate(IncomeTax.class));
    }
}
