package com.piotrwalkusz.compgraph.grapher;

import com.google.inject.grapher.InterfaceNode;
import com.google.inject.grapher.Node;
import com.google.inject.grapher.NodeId;

import java.util.Objects;

public class CustomInterfaceNode extends InterfaceNode {

    private final Object instance;

    public CustomInterfaceNode(NodeId id, Object source, Object instance) {
        super(id, source);
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomInterfaceNode)) {
            return false;
        }

        final CustomInterfaceNode other = (CustomInterfaceNode) obj;
        return super.equals(other) && Objects.equals(instance, other.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), instance);
    }

    @Override
    public Node copy(NodeId id) {
        return new CustomInterfaceNode(id, getSource(), instance);
    }
}
