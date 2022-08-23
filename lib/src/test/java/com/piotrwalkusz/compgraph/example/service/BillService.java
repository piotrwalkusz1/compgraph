package com.piotrwalkusz.compgraph.example.service;

import java.math.BigDecimal;

public interface BillService {

    BigDecimal sumUnpaidBillsInYear(int taxpayer, int year, String billType);
}
