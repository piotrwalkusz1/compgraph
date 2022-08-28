package com.piotrwalkusz.compgraph.examples.complex.node;

import com.piotrwalkusz.compgraph.Node;
import com.piotrwalkusz.compgraph.examples.complex.input.PropertyValue;

import javax.inject.Inject;
import java.math.BigDecimal;

public class PropertyTax extends Node<BigDecimal> {

    @Inject
    private PropertyValue propertyValue;

    @Inject
    private TaxCredit taxCredit;

    @Override
    protected BigDecimal evaluate() {
        return propertyValue.getValue()
                .multiply(BigDecimal.valueOf(0.02))
                .subtract(taxCredit.getValue())
                .max(BigDecimal.ZERO);
    }
}
