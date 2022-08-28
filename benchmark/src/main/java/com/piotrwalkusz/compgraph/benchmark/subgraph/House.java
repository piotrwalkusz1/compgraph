package com.piotrwalkusz.compgraph.benchmark.subgraph;

import com.piotrwalkusz.compgraph.SubgraphQualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SubgraphQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface House {
}
