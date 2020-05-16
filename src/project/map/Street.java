package project.map;

import java.util.ArrayList;
import java.util.List;

public class Street {
    public String name;
    public double costMultiplier;
    private List<Node> nodes;
    private boolean oneWay;


    public Street(String name, List<Node> nodes) {
        this.nodes = nodes;
        this.name = name;
        costMultiplier = 1.0;
        oneWay = false;
    }

    public List<Node> listNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            edges.add(new Edge(nodes.get(i), nodes.get(i + 1), costMultiplier));
        }
        return edges;
    }

    public boolean getOneWay() { return oneWay; }

    public void setOneWay(boolean oneWay) { this.oneWay = oneWay; }
}
