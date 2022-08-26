package com.piotrwalkusz.compgraph.example.input;

import com.piotrwalkusz.compgraph.DisplayableValue;
import lombok.Value;

@Value
public class TaxpayerId implements DisplayableValue {
    int value;

    @Override
    public String getDisplayedValue() {
        return Integer.toString(value);
    }
}
