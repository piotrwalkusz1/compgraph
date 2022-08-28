package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.complex.input.Income;
import com.piotrwalkusz.compgraph.examples.complex.qualifier.PreviousYear;

import javax.inject.Inject;
import java.math.BigDecimal;

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
