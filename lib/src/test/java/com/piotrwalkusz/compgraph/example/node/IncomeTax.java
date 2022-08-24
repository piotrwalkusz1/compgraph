package com.piotrwalkusz.compgraph.example.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.example.graph.AdditionalTaxes2021;
import com.piotrwalkusz.compgraph.example.graph.AdditionalTaxes2022;
import com.piotrwalkusz.compgraph.example.input.Income;
import com.piotrwalkusz.compgraph.example.qualifier.PreviousYear;
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
    private AdditionalTaxes2021 additionalTaxes2021;

    @Inject
    private AdditionalTaxes2022 additionalTaxes2022;

    @Override
    protected BigDecimal evaluate() {
        final BigDecimal incomeTax = income.getValue()
                .multiply(incomeTaxRate.getValue())
                .subtract(incomeTaxCredit.getValue())
                .max(BigDecimal.ZERO)
                .add(incomeUnpaidBillsAmount.evaluate())
                .add(additionalTaxes2021.getAdditionalIncomeTax())
                .add(additionalTaxes2022.getAdditionalIncomeTax());
        if (incomeTax.compareTo(BigDecimal.ZERO) == 0 && incomeTaxInPreviousYear.getValue().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(1000.0);
        }
        return incomeTax;
    }

    @Override
    public String getDisplayedValue() {
        return getValue().toString();
    }
}
