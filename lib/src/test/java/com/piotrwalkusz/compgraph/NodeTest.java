package com.piotrwalkusz.compgraph;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeTest {

    private static final String NODE_EVALUATION_RESULT = "NODE_EVALUATION_RESULT";
    private static final String NODE_FIX_VALUE = "NODE_FIX_VALUE";

    @NoArgsConstructor
    public static final class MockNode extends Node<String> {

        public MockNode(String value) {
            super(value);
        }

        @Getter
        private int counter = 0;

        @Override
        protected String evaluate() {
            counter++;
            return "NODE_EVALUATION_RESULT";
        }
    }

    @Test
    void shouldInvokeEvaluateMethodAndReturnEvaluatedValue() {
        final MockNode node = new MockNode();

        final String value = node.getValue();

        assertEquals(NODE_EVALUATION_RESULT, value);
        assertEquals(1, node.getCounter());
    }

    @Test
    void shouldNotInvokeEvaluateMethodSecondTimeAndReturnCachedValue() {
        final MockNode node = new MockNode();

        node.getValue();
        final String value = node.getValue();

        assertEquals(NODE_EVALUATION_RESULT, value);
        assertEquals(1, node.getCounter());
    }

    @Test
    void shouldNotInvokeEvaluateMethodAndReturnValueFromConstructor() {
        final MockNode node = new MockNode(NODE_FIX_VALUE);

        final String value = node.getValue();

        assertEquals(NODE_FIX_VALUE, value);
        assertEquals(0, node.getCounter());
    }
}
