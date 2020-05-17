package project.map;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Data class for node</h2>
 *
 * Node is pair of coordinates and 0-1 stop
 */
public class Node {
    /**
     * x coordinate of this node
     */
    public double x;
    /**
     * y coordinate of this node
     */
    public double y;
    /**
     * stop on this node
     *
     * if this node is not a stop, this variable will remain null
     */
    public Stop stop;

    /**
     * Constructor for cases with no stop
     * @param x x coordinate
     * @param y y coordinate
     */
    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        stop = null;
    }

    /**
     * Constructor for cases with stop
     * @param x x coordinate
     * @param y y coordinate
     * @param stop stop
     */
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

    /**
     * Method for finding neighbouring nodes
     * @param m map data
     * @return list of neighbours
     */
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

    /**
     * Method for finding neighbouring nodes, no matter if closed
     * @param m map data
     * @return list of neighbours
     */
    public List<Node> neighbours_unclosed(Map m) {
        List<Node> neighbours = new ArrayList<>();
        for (Street s: m.streets) {
            List<Node> nodes = s.listNodes();
            if (nodes.contains(this)) {
                int i = nodes.indexOf(this);
                if (i != 0) {
                    neighbours.add(nodes.get(i - 1));
                }
                if (i != nodes.size() - 1) {
                    neighbours.add(nodes.get(i + 1));
                }
            }
        }
        return neighbours;
    }

    /**
     * Computes distance between nodes
     * @param other other node
     * @return distance
     */
    public Double getCost(Node other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }
}
