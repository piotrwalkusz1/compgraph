package com.piotrwalkusz.compgraph.example.input;

import com.piotrwalkusz.compgraph.DisplayableValue;
import lombok.Value;

import java.time.LocalDate;

@Value
public class CalculationDate implements DisplayableValue {
    LocalDate value;

    @Override
    public String getDisplayedValue() {
        return value.toString();
    }
}
