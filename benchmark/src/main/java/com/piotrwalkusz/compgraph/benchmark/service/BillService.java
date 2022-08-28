package com.piotrwalkusz.compgraph.benchmark.service;

import java.util.HashMap;
import java.util.Map;

public class BillService {

    private final Map<Integer, Integer> unpaidBillsByTaxPayerIds;

    public BillService() {
        unpaidBillsByTaxPayerIds = new HashMap<>();
        unpaidBillsByTaxPayerIds.put(1, 0);
        unpaidBillsByTaxPayerIds.put(2, 10);
    }

    public int getNumberOfUnpaidBillsByTaxPayerId(int taxPayerId) {
        return unpaidBillsByTaxPayerIds.get(taxPayerId);
    }
}
