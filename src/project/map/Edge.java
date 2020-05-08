package project.map;

import com.sun.istack.internal.NotNull;

public class Edge {
    public Node start;
    public Node end;
    public double cost;
    public boolean oneWay;
    public boolean closed;

    public Edge(@NotNull Node start, @NotNull Node end) {
        if (start.equals(end)) {
            throw new IllegalArgumentException("start cannot be the same as end");
        }
        this.start = start;
        this.end = end;
        this.cost = Math.sqrt(Math.pow(start.x - end.x, 2)  + Math.pow(start.y - end.y, 2));
        closed = false;
    }
}
