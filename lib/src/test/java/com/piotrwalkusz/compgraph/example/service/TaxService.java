package com.piotrwalkusz.compgraph.example.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxService {

    public BigDecimal getIncomeTaxRateService(LocalDate calculationDate) {
        if (calculationDate.getYear() >= 2022) {
            return BigDecimal.valueOf(0.19);
        } else {
            return BigDecimal.valueOf(0.17);
        }
    }

    public BigDecimal getMaxIncomeTaxCredit(LocalDate calculationDate) {
        if (calculationDate.getYear() >= 2021) {
            return BigDecimal.valueOf(25000);
        } else {
            return BigDecimal.valueOf(12000);
        }
    }
}
