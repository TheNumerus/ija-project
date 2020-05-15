package project.map;

import com.sun.istack.internal.NotNull;
import project.gui.EdgeLine;

import java.util.ArrayList;
import java.util.List;

public class Street {
    public String name;
    public double costMultiplier;
    private List<EdgeLine> edgeLines;
    private List<Node> nodes;
    private boolean oneWay;


    public Street(@NotNull String name, List<Node> nodes) {
        this.nodes = nodes;
        this.name = name;
        costMultiplier = 1.0;
        oneWay = false;
        edgeLines = new ArrayList<EdgeLine>();
    }

    public void addEdgeLine(EdgeLine el){
        this.edgeLines.add(el);
    }

    public List<EdgeLine> getEdgeLines(){
        return this.edgeLines;
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
