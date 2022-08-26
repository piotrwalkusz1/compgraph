package com.piotrwalkusz.compgraph.example;

import com.piotrwalkusz.compgraph.Graph;
import com.piotrwalkusz.compgraph.example.input.CalculationDate;
import com.piotrwalkusz.compgraph.example.input.Income;
import com.piotrwalkusz.compgraph.example.input.TaxpayerId;
import com.piotrwalkusz.compgraph.example.input.Year;
import com.piotrwalkusz.compgraph.example.node.IncomeTax;
import com.piotrwalkusz.compgraph.example.qualifier.PreviousYear;
import com.piotrwalkusz.compgraph.example.service.BillServiceImpl;
import com.piotrwalkusz.compgraph.example.service.TaxService;
import com.piotrwalkusz.compgraph.example.subgraph.Year2021;
import com.piotrwalkusz.compgraph.example.subgraph.Year2022;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExampleTest {

    @Test
    void test() {
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

        graph.evaluate(IncomeTax.class);
    }

    @Test
    void test2() {
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

        graph.evaluate(IncomeTax.class);
        graph.draw();
    }
}
