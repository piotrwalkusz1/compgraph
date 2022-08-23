package com.piotrwalkusz.compgraph.example.node;

public class IncomeUnpaidBillsAmount extends UnpaidBillsAmount {

    public IncomeUnpaidBillsAmount() {
        super("income");
    }

    @Override
    public String getDisplayedValue() {
        return getValue().toString();
    }
}
