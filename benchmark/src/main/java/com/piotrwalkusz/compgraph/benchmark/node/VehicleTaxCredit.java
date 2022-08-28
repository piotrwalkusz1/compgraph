package com.piotrwalkusz.compgraph.benchmark.node;

import com.piotrwalkusz.compgraph.benchmark.input.PropertyValue;

import javax.inject.Inject;
import java.math.BigDecimal;

public class VehicleTaxCredit extends TaxCredit {

    @Inject
    private UnpaidBillsCount unpaidBillsCount;

    @Inject
    private PropertyValue propertyValue;

    @Override
    protected BigDecimal evaluate() {
        if (unpaidBillsCount.getValue() > 5) {
            if (propertyValue.getValue().compareTo(BigDecimal.valueOf(50000)) > 0) {
                return BigDecimal.ZERO;
            } else {
                return BigDecimal.valueOf(1000);
            }
        } else {
            if (propertyValue.getValue().compareTo(BigDecimal.valueOf(40000)) > 0) {
                return BigDecimal.valueOf(500);
            } else {
                return BigDecimal.valueOf(3000);
            }
        }
    }
}
