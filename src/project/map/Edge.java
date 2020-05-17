package project.map;

/**
 * Data class for edges between two nodes
 *
 * Mostly used as a helper for rendering
 */
public class Edge {
    /**
     * starting point of this edge
     */
    public Node start;
    /**
     * ending point of this edge
     */
    public Node end;
    /**
     * cost of this edge
     */
    public double cost;

    /**
     * Contstructor of Edge
     * @param start starting point of edge
     * @param end ending point of edge
     * @param costMult cost multiplier
     */
    public Edge(Node start, Node end, double costMult) {
        if (start.equals(end)) {
            throw new IllegalArgumentException("start cannot be the same as end");
        }
        this.start = start;
        this.end = end;
        this.cost = Math.sqrt(Math.pow(start.x - end.x, 2)  + Math.pow(start.y - end.y, 2)) * costMult;
    }
}
