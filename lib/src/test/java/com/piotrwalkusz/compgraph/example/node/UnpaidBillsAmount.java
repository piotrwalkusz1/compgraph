package com.piotrwalkusz.compgraph.example.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.example.input.CalculationDate;
import com.piotrwalkusz.compgraph.example.input.TaxpayerId;
import com.piotrwalkusz.compgraph.example.service.BillService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class UnpaidBillsAmount extends Node<BigDecimal> {

    private final String billType;

    @Inject
    private BillService billService;

    @Inject
    private TaxpayerId taxpayerId;

    @Inject
    private CalculationDate calculationDate;

    @Override
    protected BigDecimal evaluate() {
        return billService.sumUnpaidBillsInYear(taxpayerId.getValue(), calculationDate.getValue().getYear() - 1, billType);
    }
}
