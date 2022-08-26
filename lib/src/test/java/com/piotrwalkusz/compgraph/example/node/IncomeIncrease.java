package com.piotrwalkusz.compgraph.example.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.example.input.Income;
import com.piotrwalkusz.compgraph.example.qualifier.PreviousYear;

import javax.inject.Inject;
import java.math.BigDecimal;

public class IncomeIncrease extends Node<BigDecimal> {

    @Inject
    private Income income;

    @Inject
    @PreviousYear
    private Income incomeInPreviousYear;

    @Override
    protected BigDecimal evaluate() {
        return income.getValue().subtract(incomeInPreviousYear.getValue());
    }
}
