package project.map;

import com.sun.istack.internal.NotNull;

public class Edge {
    public Node start;
    public Node end;
    public int cost;
    public boolean oneWay;

    public Edge(@NotNull Node start, @NotNull Node end, int cost, boolean oneWay) {
        if (start.equals(end)) {
            throw new IllegalArgumentException("start cannot be the same as end");
        }
        this.start = start;
        this.end = end;
        this.cost = cost;
        this.oneWay = oneWay;
    }
}
