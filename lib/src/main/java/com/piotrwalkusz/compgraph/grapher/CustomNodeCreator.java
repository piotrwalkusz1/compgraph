package com.piotrwalkusz.compgraph.grapher;

import com.google.common.collect.ImmutableList;
import com.google.inject.Binding;
import com.google.inject.grapher.*;
import com.google.inject.spi.*;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomNodeCreator implements NodeCreator {

    @Override
    public Iterable<Node> getNodes(Iterable<Binding<?>> bindings) {
        final List<Node> nodes = new ArrayList<>();
        final NodeVisitor visitor = new NodeVisitor();
        for (Binding<?> binding : bindings) {
            nodes.addAll(binding.acceptTargetVisitor(visitor));
        }

        return nodes;
    }

    private static final class NodeVisitor extends DefaultBindingTargetVisitor<Object, Collection<Node>> {

        private InterfaceNode newInterfaceNode(Binding<?> binding) {
            return new CustomInterfaceNode(NodeId.newTypeId(binding.getKey()), binding.getSource(), binding.getProvider().get());
        }

        private ImplementationNode newImplementationNode(Binding<?> binding, Collection<Member> members) {
            return new CustomImplementationNode(NodeId.newTypeId(binding.getKey()), binding.getSource(), members, binding.getProvider().get());
        }

        private <T extends Binding<?> & HasDependencies> InstanceNode newInstanceNode(T binding, Object instance) {
            final Collection<Member> members = new ArrayList<>();
            for (Dependency<?> dependency : binding.getDependencies()) {
                final InjectionPoint injectionPoint = dependency.getInjectionPoint();
                if (injectionPoint != null) {
                    members.add(injectionPoint.getMember());
                }
            }

            return new InstanceNode(NodeId.newInstanceId(binding.getKey()), binding.getSource(), instance, members);
        }

        @Override
        public Collection<Node> visit(ConstructorBinding<?> binding) {
            final Collection<Member> members = new ArrayList<>();
            members.add(binding.getConstructor().getMember());
            for (InjectionPoint injectionPoint : binding.getInjectableMembers()) {
                members.add(injectionPoint.getMember());
            }

            return ImmutableList.of(newImplementationNode(binding, members));
        }

        @Override
        public Collection<Node> visit(InstanceBinding<?> binding) {
            return ImmutableList.of(newInterfaceNode(binding));
        }

        @Override
        public Collection<Node> visit(ProviderInstanceBinding<?> binding) {
            return ImmutableList.of(newInterfaceNode(binding));
        }

        @Override
        public Collection<Node> visitOther(Binding<?> binding) {
            return ImmutableList.of(newInterfaceNode(binding));
        }
    }
}
