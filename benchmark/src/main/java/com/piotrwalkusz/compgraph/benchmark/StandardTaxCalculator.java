package com.piotrwalkusz.compgraph.benchmark;

import java.math.BigDecimal;

public class StandardTaxCalculator {

    public BigDecimal calculateTax(TaxCalculationParams params) {
        final int unpaidBillsCount = params.getBillService().getNumberOfUnpaidBillsByTaxPayerId(params.getTaxPayerId());
        final boolean incomeDecreased = params.getIncomeInCurrentYear().compareTo(params.getIncomeInPreviousYear()) < 0;
        final BigDecimal additionalIncomeTax = params.getTaxService().getAdditionalIncomeTaxByYear(params.getYear());
        final BigDecimal incomeTaxCredit = params.getChildrenCount() > 0 && incomeDecreased ? BigDecimal.valueOf(2000) : BigDecimal.ZERO;
        final BigDecimal incomeTax = params.getIncomeInCurrentYear()
                .multiply(BigDecimal.valueOf(0.2))
                .add(additionalIncomeTax)
                .subtract(incomeTaxCredit)
                .max(BigDecimal.ZERO);
        final BigDecimal houseTaxCredit = BigDecimal.valueOf(100000)
                .subtract(params.getIncomeInCurrentYear())
                .max(BigDecimal.ZERO)
                .add(BigDecimal.valueOf(10000).multiply(BigDecimal.valueOf(params.getChildrenCount())));
        final BigDecimal houseTax = params.getHouseValue()
                .multiply(BigDecimal.valueOf(0.02))
                .subtract(houseTaxCredit)
                .max(BigDecimal.ZERO);
        final BigDecimal vehicleTaxCredit = getVehicleTaxCredit(unpaidBillsCount, params.getVehicleValue());
        final BigDecimal vehicleTax = params.getVehicleValue()
                .multiply(BigDecimal.valueOf(0.02))
                .subtract(vehicleTaxCredit)
                .max(BigDecimal.ZERO);

        return incomeTax.add(houseTax).add(vehicleTax);
    }

    private BigDecimal getVehicleTaxCredit(int unpaidBillsCount, BigDecimal vehicleValue) {
        if (unpaidBillsCount > 5) {
            if (vehicleValue.compareTo(BigDecimal.valueOf(50000)) > 0) {
                return BigDecimal.ZERO;
            } else {
                return BigDecimal.valueOf(1000);
            }
        } else {
            if (vehicleValue.compareTo(BigDecimal.valueOf(40000)) > 0) {
                return BigDecimal.valueOf(500);
            } else {
                return BigDecimal.valueOf(3000);
            }
        }
    }
}
