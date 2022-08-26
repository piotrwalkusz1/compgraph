package com.piotrwalkusz.compgraph.example.input;

import com.piotrwalkusz.compgraph.DisplayableValue;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Income implements DisplayableValue {
    BigDecimal value;

    @Override
    public String getDisplayedValue() {
        return value.toString();
    }
}
