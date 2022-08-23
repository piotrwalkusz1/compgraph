package com.piotrwalkusz.compgraph.example.input;

import com.piotrwalkusz.compgraph.Input;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Income implements Input {
    BigDecimal value;

    @Override
    public String getDisplayedValue() {
        return value.toString();
    }
}
