package project.map;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public double x;
    public double y;
    public Stop stop;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        stop = null;
    }

    public Node(double x, double y, Stop stop) {
        this.x = x;
        this.y = y;
        this.stop = stop;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node other = (Node)o;
        return x == other.x && y == other.y;
    }

    public List<Node> neighbours(Map m) {
        List<Node> neighbours = new ArrayList<>();
        for (Street s: m.streets) {
            List<Node> nodes = s.listNodes();
            if (nodes.contains(this)) {
                int i = nodes.indexOf(this);
                if (i != 0 && !m.isEdgeClosed(this, nodes.get(i - 1))) {
                    neighbours.add(nodes.get(i - 1));
                }
                if (i != nodes.size() - 1 && !m.isEdgeClosed(this, nodes.get(i + 1))) {
                    neighbours.add(nodes.get(i + 1));
                }
            }
        }
        return neighbours;
    }

    public Double getCost(Node other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }
}
