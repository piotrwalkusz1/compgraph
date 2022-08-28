package com.piotrwalkusz.compgraph.benchmark.node;

import com.piotrwalkusz.compgraph.benchmark.input.Income;
import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.benchmark.qualifier.PreviousYear;

import javax.inject.Inject;

public class IncomeDecreased extends Node<Boolean> {

    @Inject
    private Income incomeInCurrentYear;

    @Inject
    @PreviousYear
    private Income incomeInPreviousYear;

    @Override
    protected Boolean evaluate() {
        return incomeInCurrentYear.getValue().compareTo(incomeInPreviousYear.getValue()) < 0;
    }
}
