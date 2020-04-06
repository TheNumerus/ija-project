package project.map;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Street {
    private List<Edge> edges;
    private Node currentStart;
    private Node currentEnd;
    public String name;
    public double costMultiplier;

    public Street(@NotNull Edge e, @NotNull String name) {
        edges = new ArrayList<>();
        edges.add(e);
        currentStart = e.start;
        currentEnd = e.end;
        this.name = name;
        costMultiplier = 1.0;
    }

    public void addEdge(@NotNull Edge e) {
        if (edges.contains(e)) {
            return;
        }
        if (currentEnd.equals(e.start)) {
            currentEnd = e.end;
            edges.add(e);
        } else if (currentStart.equals(e.end)) {
            currentStart = e.start;
            edges.add(0, e);
        } else {
            if (e.oneWay) {
                throw new IllegalArgumentException();
            } else if (currentEnd.equals(e.end)) {
                currentEnd = e.end;
                edges.add(e);
            } else if (currentStart.equals(e.start)) {
                currentStart = e.start;
                edges.add(0, e);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<Node> listNodes() {
        List<Node> list = new ArrayList<>();
        list.add(currentStart);
        for (Edge e: edges) {
            list.add(e.end);
        }
        return list;
    }
}
