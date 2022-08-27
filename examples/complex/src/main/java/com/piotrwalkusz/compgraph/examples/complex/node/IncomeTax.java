package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.complex.input.Income;
import com.piotrwalkusz.compgraph.examples.complex.qualifier.PreviousYear;
import com.piotrwalkusz.compgraph.examples.complex.subgraph.Year2021;
import com.piotrwalkusz.compgraph.examples.complex.subgraph.Year2022;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import java.math.BigDecimal;

@NoArgsConstructor
public class IncomeTax extends Node<BigDecimal> {

    public IncomeTax(BigDecimal value) {
        super(value);
    }

    @Inject
    private Income income;

    @Inject
    private IncomeTaxRate incomeTaxRate;

    @Inject
    private IncomeTaxCredit incomeTaxCredit;

    @Inject
    @PreviousYear
    private IncomeTax incomeTaxInPreviousYear;

    @Inject
    private IncomeUnpaidBillsAmount incomeUnpaidBillsAmount;

    @Inject
    @Year2021
    private AdditionalIncomeTax additionalIncomeTax2021;

    @Inject
    @Year2022
    private AdditionalIncomeTax additionalIncomeTax2022;

    @Override
    protected BigDecimal evaluate() {
        final BigDecimal incomeTax = income.getValue()
                .multiply(incomeTaxRate.getValue())
                .subtract(incomeTaxCredit.getValue())
                .max(BigDecimal.ZERO)
                .add(incomeUnpaidBillsAmount.evaluate())
                .add(additionalIncomeTax2021.getValue())
                .add(additionalIncomeTax2022.getValue());
        if (incomeTax.compareTo(BigDecimal.ZERO) == 0 && incomeTaxInPreviousYear.getValue().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(1000.0);
        }
        return incomeTax;
    }
}
