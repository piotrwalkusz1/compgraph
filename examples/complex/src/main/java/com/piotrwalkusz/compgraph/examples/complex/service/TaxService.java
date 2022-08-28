package com.piotrwalkusz.compgraph.examples.complex.service;

import java.math.BigDecimal;

public class TaxService {

    public BigDecimal getAdditionalIncomeTaxByYear(int year) {
        return year >= 2022 ? BigDecimal.valueOf(1000) : BigDecimal.valueOf(500);
    }
}
