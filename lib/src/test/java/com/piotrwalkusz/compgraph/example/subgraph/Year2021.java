package com.piotrwalkusz.compgraph.example.subgraph;

import com.piotrwalkusz.compgraph.SubGraphQualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SubGraphQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Year2021 {
}
