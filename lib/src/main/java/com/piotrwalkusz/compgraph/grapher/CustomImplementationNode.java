package com.piotrwalkusz.compgraph.grapher;

import com.google.inject.grapher.ImplementationNode;
import com.google.inject.grapher.Node;
import com.google.inject.grapher.NodeId;

import java.lang.reflect.Member;
import java.util.Collection;
import java.util.Objects;

public class CustomImplementationNode extends ImplementationNode {

    private final Object instance;

    public CustomImplementationNode(NodeId id, Object source, Collection<Member> members, Object instance) {
        super(id, source, members);
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomImplementationNode)) {
            return false;
        }

        final CustomImplementationNode other = (CustomImplementationNode) obj;
        return super.equals(other) && Objects.equals(instance, other.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), instance);
    }

    @Override
    public Node copy(NodeId id) {
        return new CustomImplementationNode(id, getSource(), getMembers(), instance);
    }
}
