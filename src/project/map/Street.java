package project.map;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Street {
    public String name;
    public double costMultiplier;
    private List<Edge> edges;
    private boolean oneWay;


    public Street(@NotNull String name, List<Node> nodes) {
        edges = new ArrayList<>();
        for(int i = 0; i < nodes.size() - 1; i++) {
            edges.add(new Edge(nodes.get(i), nodes.get(i + 1)));
        }
        this.name = name;
        costMultiplier = 1.0;
        oneWay = false;
    }

    public List<Node> listNodes() {
        List<Node> list = new ArrayList<>();
        list.add(edges.get(0).start);
        for (Edge e: edges) {
            list.add(e.end);
        }
        return list;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public boolean getOneWay() { return oneWay; }

    public void setOneWay(boolean oneWay) { this.oneWay = oneWay; }
}
