package com.piotrwalkusz.compgraph.grapher;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.grapher.*;
import com.google.inject.grapher.graphviz.*;
import com.google.inject.spi.InjectionPoint;
import com.piotrwalkusz.compgraph.DisplayableValue;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomGraphvizGrapher extends AbstractInjectorGrapher {
    private final Map<NodeId, GraphvizNode> nodes = new HashMap<>();
    private final List<GraphvizEdge> edges = new ArrayList<>();
    private final NameFactory nameFactory;
    private final PortIdFactory portIdFactory;

    private PrintWriter out;

    public CustomGraphvizGrapher(NameFactory nameFactory, PortIdFactory portIdFactory) {
        super(new GrapherParameters().setNodeCreator(new CustomNodeCreator()));
        this.nameFactory = nameFactory;
        this.portIdFactory = portIdFactory;
    }

    @Override
    protected void reset() {
        nodes.clear();
        edges.clear();
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    @Override
    protected void postProcess() {
        start();

        for (GraphvizNode node : nodes.values()) {
            renderNode(node);
        }

        for (GraphvizEdge edge : edges) {
            renderEdge(edge);
        }

        finish();

        out.flush();
    }

    protected Map<String, String> getGraphAttributes() {
        final Map<String, String> attrs = new HashMap<>();
        attrs.put("rankdir", "TB");
        return attrs;
    }

    protected void start() {
        out.println("digraph injector {");

        Map<String, String> attrs = getGraphAttributes();
        out.println("graph " + getAttrString(attrs) + ";");
    }

    protected void finish() {
        out.println("}");
    }

    protected void renderNode(GraphvizNode node) {
        Map<String, String> attrs = getNodeAttributes(node);
        out.println(node.getIdentifier() + " " + getAttrString(attrs));
    }

    protected Map<String, String> getNodeAttributes(GraphvizNode node) {
        Map<String, String> attrs = Maps.newHashMap();

        attrs.put("label", getNodeLabel(node));
        // remove most of the margin because the table has internal padding
        attrs.put("margin", "\"0.02,0\"");
        attrs.put("shape", node.getShape().toString());
        attrs.put("style", node.getStyle().toString());

        return attrs;
    }

    protected String getNodeLabel(GraphvizNode graphvizNode) {
        final CustomGraphvizNode node = (CustomGraphvizNode) graphvizNode;
        final String cellborder = node.getStyle() == NodeStyle.INVISIBLE ? "1" : "0";

        final StringBuilder htmlBuilder = new StringBuilder()
                .append("<")
                .append("<table cellspacing=\"0\" cellpadding=\"5\" cellborder=\"").append(cellborder).append("\" border=\"0\">")
                .append("<tr>")
                .append("<td align=\"left\" port=\"header\" ")
                .append("bgcolor=\"")
                .append(node.getHeaderBackgroundColor())
                .append("\">");

        if (node.getAnnotation() != null) {
            htmlBuilder
                    .append("<font color=\"").append(node.getHeaderTextColor()).append("\" point-size=\"10\">")
                    .append(htmlEscape(node.getAnnotation()))
                    .append("<br align=\"left\"/>")
                    .append("</font>");
        }

        htmlBuilder
                .append("<font color=\"")
                .append(node.getHeaderTextColor())
                .append("\">")
                .append(htmlEscape(node.getTitle()))
                .append("<br align=\"left\"/>")
                .append("</font>");

        if (node.getValue() != null) {
            htmlBuilder
                    .append("<font color=\"")
                    .append(node.getHeaderTextColor())
                    .append("\" point-size=\"10\">")
                    .append(htmlEscape(node.getValue()))
                    .append("<br align=\"left\"/>")
                    .append("</font>");
        }

        htmlBuilder
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append(">");

        return htmlBuilder.toString();
    }

    protected void renderEdge(GraphvizEdge edge) {
        final Map<String, String> attrs = getEdgeAttributes(edge);
        final GraphvizNode tailNode = nodes.get(edge.getTailNodeId());
        final GraphvizNode headNode = nodes.get(edge.getHeadNodeId());
        if (tailNode == null || headNode == null) {
            return;
        }

        String tailId =
                getEdgeEndPoint(
                        tailNode.getIdentifier(),
                        edge.getTailPortId(),
                        edge.getTailCompassPoint());

        String headId =
                getEdgeEndPoint(
                        headNode.getIdentifier(),
                        edge.getHeadPortId(),
                        edge.getHeadCompassPoint());

        out.println(tailId + " -> " + headId + " " + getAttrString(attrs));
    }

    protected Map<String, String> getEdgeAttributes(GraphvizEdge edge) {
        Map<String, String> attrs = Maps.newHashMap();

        attrs.put("arrowhead", getArrowString(edge.getArrowHead()));
        attrs.put("arrowtail", getArrowString(edge.getArrowTail()));
        attrs.put("style", edge.getStyle().toString());

        return attrs;
    }

    private String getAttrString(Map<String, String> attrs) {
        List<String> attrList = Lists.newArrayList();

        for (Map.Entry<String, String> attr : attrs.entrySet()) {
            String value = attr.getValue();

            if (value != null) {
                attrList.add(attr.getKey() + "=" + value);
            }
        }

        return "[" + Joiner.on(", ").join(attrList) + "]";
    }

    protected String getArrowString(List<ArrowType> arrows) {
        return Joiner.on("").join(arrows);
    }

    protected String getEdgeEndPoint(String nodeId, String portId, CompassPoint compassPoint) {
        List<String> portStrings = Lists.newArrayList(nodeId);

        if (portId != null) {
            portStrings.add(portId);
        }

        if (compassPoint != null) {
            portStrings.add(compassPoint.toString());
        }

        return Joiner.on(":").join(portStrings);
    }

    protected String htmlEscape(String str) {
        return str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    protected void newInterfaceNode(InterfaceNode node) {
        final NodeId nodeId = node.getId();
        final Key<?> key = nodeId.getKey();
        final String annotation = nameFactory.getAnnotationName(key);
        final CustomGraphvizNode gnode = new CustomGraphvizNode(nodeId);
        gnode.setStyle(NodeStyle.SOLID);
        gnode.setTitle(nameFactory.getClassName(key));
        if (!annotation.isEmpty()) {
            gnode.setAnnotation(annotation);
        }
        gnode.setValue(getDisplayedValue(node));

        addNode(gnode);
    }

    @Override
    protected void newImplementationNode(ImplementationNode node) {
        final NodeId nodeId = node.getId();
        final CustomGraphvizNode gnode = new CustomGraphvizNode(nodeId);
        gnode.setStyle(NodeStyle.SOLID);
        gnode.setTitle(nameFactory.getClassName(nodeId.getKey()));
        gnode.setValue(getDisplayedValue(node));

        addNode(gnode);
    }

    private String getDisplayedValue(Node node) {
        if (node instanceof CustomInterfaceNode) {
            return getDisplayedValueFromInstance(((CustomInterfaceNode) node).getInstance());
        } else if (node instanceof CustomImplementationNode) {
            return getDisplayedValueFromInstance(((CustomImplementationNode) node).getInstance());
        } else {
            return null;
        }
    }

    private String getDisplayedValueFromInstance(Object instance) {
        if (instance instanceof DisplayableValue) {
            return ((DisplayableValue) instance).getDisplayedValue();
        }
        return null;
    }

    @Override
    protected void newInstanceNode(InstanceNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void newDependencyEdge(DependencyEdge edge) {
        GraphvizEdge gedge = new GraphvizEdge(edge.getFromId(), edge.getToId());
        InjectionPoint fromPoint = edge.getInjectionPoint();
        if (fromPoint == null) {
            gedge.setTailPortId("header");
        } else {
            gedge.setTailPortId(portIdFactory.getPortId(fromPoint.getMember()));
        }
        gedge.setArrowHead(ImmutableList.of(ArrowType.NORMAL));
        gedge.setTailCompassPoint(CompassPoint.EAST);

        edges.add(gedge);
    }

    @Override
    protected void newBindingEdge(BindingEdge edge) {
        GraphvizEdge gedge = new GraphvizEdge(edge.getFromId(), edge.getToId());
        gedge.setStyle(EdgeStyle.DASHED);
        switch (edge.getType()) {
            case NORMAL:
                gedge.setArrowHead(ImmutableList.of(ArrowType.NORMAL_OPEN));
                break;

            case PROVIDER:
                gedge.setArrowHead(ImmutableList.of(ArrowType.NORMAL_OPEN, ArrowType.NORMAL_OPEN));
                break;

            case CONVERTED_CONSTANT:
                gedge.setArrowHead(ImmutableList.of(ArrowType.NORMAL_OPEN, ArrowType.DOT_OPEN));
                break;
        }

        edges.add(gedge);
    }

    private void addNode(GraphvizNode node) {
        node.setIdentifier("x" + nodes.size());
        nodes.put(node.getNodeId(), node);
    }
}