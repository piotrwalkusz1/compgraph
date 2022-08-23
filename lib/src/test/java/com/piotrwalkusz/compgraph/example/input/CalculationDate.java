package com.piotrwalkusz.compgraph.example.input;

import com.piotrwalkusz.compgraph.Input;
import lombok.Value;

import java.time.LocalDate;

@Value
public class CalculationDate implements Input {
    LocalDate value;

    @Override
    public String getDisplayedValue() {
        return value.toString();
    }
}
