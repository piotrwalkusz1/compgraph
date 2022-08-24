package com.piotrwalkusz.compgraph.example;

import com.piotrwalkusz.compgraph.Graph;
import com.piotrwalkusz.compgraph.GraphBuilder;
import com.piotrwalkusz.compgraph.example.graph.AdditionalTaxes2021;
import com.piotrwalkusz.compgraph.example.graph.AdditionalTaxes2022;
import com.piotrwalkusz.compgraph.example.input.CalculationDate;
import com.piotrwalkusz.compgraph.example.input.Income;
import com.piotrwalkusz.compgraph.example.input.TaxpayerId;
import com.piotrwalkusz.compgraph.example.node.IncomeTax;
import com.piotrwalkusz.compgraph.example.qualifier.PreviousYear;
import com.piotrwalkusz.compgraph.example.service.BillService;
import com.piotrwalkusz.compgraph.example.service.BillServiceImpl;
import com.piotrwalkusz.compgraph.example.service.TaxService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTest {

    @Test
    void test() {
        final Graph graph = new GraphBuilder()
                .addInput(new CalculationDate(LocalDate.of(2022, 1, 1)))
                .addInput(new Income(BigDecimal.valueOf(30000)))
                .addInput(new TaxpayerId(12))
                .addInput(new Income(BigDecimal.valueOf(30000)))
                .addInputWithAnnotation(new Income(BigDecimal.valueOf(1223)), PreviousYear.class)
                .addInputWithAnnotation(new IncomeTax(BigDecimal.valueOf(1200)), PreviousYear.class)
                .addInput(new TaxService())
                .addInput(new BillServiceImpl(), BillService.class)
                .addSubGraph(new AdditionalTaxes2021())
                .addSubGraph(new AdditionalTaxes2022())
                .addNode(IncomeTax.class)
                .build();

        final BigDecimal incomeTax = graph.evaluate(IncomeTax.class);
        graph.evaluate(IncomeTax.class);

        assertEquals(BigDecimal.valueOf(12000), incomeTax);
    }

    @Test
    @SneakyThrows
    void test2() {
        final Graph graph = new GraphBuilder()
                .addInput(new CalculationDate(LocalDate.of(2022, 1, 1)))
                .addInput(new Income(BigDecimal.valueOf(30000)))
                .addInput(new TaxpayerId(12))
                .addInput(new Income(BigDecimal.valueOf(30000)))
                .addInputWithAnnotation(new Income(BigDecimal.valueOf(1223)), PreviousYear.class)
                .addInputWithAnnotation(new IncomeTax(BigDecimal.valueOf(1200)), PreviousYear.class)
                .addInput(new TaxService())
                .addInput(new BillServiceImpl(), BillService.class)
                .addNode(IncomeTax.class)
                .build();

        graph.drawGraph("abc.dot");
    }
}
