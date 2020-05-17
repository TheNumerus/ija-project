/*
soubor: project.map.Street.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: reprezentuje ulici a její informace
 */

package project.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class for street
 */
public class Street {
    /**
     * street name
     */
    public String name;
    /**
     * cost multiplier of this street
     */
    public double costMultiplier;

    private final List<Node> nodes;

    private boolean oneWay;


    /**
     * constructor of street
     * one way is by default set to false
     * @param name name of the street
     * @param nodes list of nodes, this street is going through
     */
    public Street(String name, List<Node> nodes) {
        this.nodes = nodes;
        this.name = name;
        costMultiplier = 1.0;
        oneWay = false;
    }

    /**
     * Lists all nodes in street
     * @return nodes
     */
    public List<Node> listNodes() {
        return nodes;
    }

    /**
     * Lists all edges in street
     * @return edges
     */
    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            edges.add(new Edge(nodes.get(i), nodes.get(i + 1), costMultiplier));
        }
        return edges;
    }

    /**
     * Gets one way flag
     *
     * Curently not used in any way
     * @return one way flag
     */
    public boolean getOneWay() { return oneWay; }

    /**
     * Sets one way flag
     *
     * Curently not used in any way
     * @param oneWay new one way flag
     */
    public void setOneWay(boolean oneWay) { this.oneWay = oneWay; }
}
