package com.piotrwalkusz.compgraph.example.input;

import com.piotrwalkusz.compgraph.Input;
import lombok.Value;

@Value
public class Year implements Input {
    int value;

    @Override
    public String getDisplayedValue() {
        return Integer.toString(value);
    }
}
