package com.piotrwalkusz.compgraph.benchmark.node;

import com.piotrwalkusz.compgraph.benchmark.input.TaxPayerId;
import com.piotrwalkusz.compgraph.benchmark.service.BillService;
import com.piotrwalkusz.compgraph.Node;

import javax.inject.Inject;

public class UnpaidBillsCount extends Node<Integer> {

    @Inject
    private TaxPayerId taxPayerId;

    @Inject
    private BillService billService;

    @Override
    protected Integer evaluate() {
        return billService.getNumberOfUnpaidBillsByTaxPayerId(taxPayerId.getValue());
    }
}
