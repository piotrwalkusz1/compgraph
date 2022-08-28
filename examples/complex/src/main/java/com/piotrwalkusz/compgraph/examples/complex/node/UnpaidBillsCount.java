package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.complex.input.TaxPayerId;
import com.piotrwalkusz.compgraph.examples.complex.service.BillService;

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
