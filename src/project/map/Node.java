package project.map;

public class Node {
    public double x;
    public double y;
    public Stop stop;
    public boolean closed;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        stop = null;
        closed = false;
    }

    public Node(double x, double y, Stop stop) {
        this.x = x;
        this.y = y;
        this.stop = stop;
        closed = false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node other = (Node)o;
        return x == other.x && y == other.y;
    }
}
