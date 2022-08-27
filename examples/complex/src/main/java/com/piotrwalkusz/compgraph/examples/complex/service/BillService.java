package com.piotrwalkusz.compgraph.examples.complex.service;

import java.math.BigDecimal;

public interface BillService {

    BigDecimal sumUnpaidBillsInYear(int taxpayer, int year, String billType);
}
