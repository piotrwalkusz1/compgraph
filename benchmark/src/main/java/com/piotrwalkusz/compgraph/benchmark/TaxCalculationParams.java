package com.piotrwalkusz.compgraph.benchmark;

import com.piotrwalkusz.compgraph.benchmark.service.BillService;
import com.piotrwalkusz.compgraph.benchmark.service.TaxService;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TaxCalculationParams {
    private final BillService billService = new BillService();
    private final TaxService taxService = new TaxService();
    private final int childrenCount = 2;
    private final BigDecimal incomeInCurrentYear = BigDecimal.valueOf(100000);
    private final BigDecimal incomeInPreviousYear = BigDecimal.valueOf(110000);
    private final int taxPayerId = 2;
    private final int year = 2022;
    private final BigDecimal houseValue = BigDecimal.valueOf(700000);
    private final BigDecimal vehicleValue = BigDecimal.valueOf(900000);
}
