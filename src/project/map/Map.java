/*
soubor: project.map.Map.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: obsahuje veškeré prvky, vyskytující se na mapě (uzly, ulice, zastávky, vozidla, uzavírky, ...) a obsluhuje je.
 */

package project.map;

import project.Pair;

import java.time.Duration;
import java.util.*;

/**
 * Master data class for all map things
 *
 * Contains all nodes, vehicles, closures etc.
 */
public class Map {
    /**
     * Street list
     */
    public final List<Street> streets;
    /**
     * Stop list
     */
    public final List<Node> stops;
    /**
     * Line list
     */
    public final List<Line> lines;
    /**
     * Closure list
     */
    public List<Pair<Node, Node>> closures;
    /**
     * Vehicle list
     */
    public List<Vehicle> vehicles;

    /**
     * Detours list
     */
    public List<Pair<List<Node>, List<Node>>> detours;


    /**
     * constructor of map
     * initializes all required variables
     */
    public Map() {
        streets = new ArrayList<>();
        stops = new ArrayList<>();
        lines = new ArrayList<>();
        closures = new ArrayList<>();
        vehicles = new ArrayList<>();
        detours = new ArrayList<>();
    }

    /**
     * A* pathfinding between two routes
     * @param start start node
     * @param finish finish node
     * @return route, null if route does not exist
     */
    public List<Node> getRoute(Node start, Node finish) {
        List<Node> route = new ArrayList<>();
        PriorityQueue<Pair<Node, Double>> frontier = new PriorityQueue<>(10, Comparator.comparingDouble(Pair::getY));
        frontier.add(new Pair<>(start, 0.0));
        HashMap<Node, Node> cameFrom = new HashMap<>();
        HashMap<Node, Double> costSoFar = new HashMap<>();
        cameFrom.put(start, null);
        costSoFar.put(start, 0.0);

        while(!frontier.isEmpty()) {
            Pair<Node, Double> current = frontier.poll();
            Node currentNode = current.getX();

            if(currentNode == finish) {
                break;
            }

            for (Node next: currentNode.neighbours(this)) {
                double newCost = costSoFar.get(currentNode) + currentNode.getCost(next);
                if(!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    double priority = newCost + finish.getCost(next);
                    frontier.add(new Pair<>(next, priority));
                    cameFrom.put(next, currentNode);
                }
            }
        }
        Node from = finish;
        while(from != start) {
            route.add(from);
            from = cameFrom.get(from);
            if (from == null) {
                return null;
            }
        }
        route.add(start);

        Collections.reverse(route);

        return route;
    }

    /**
     * Helper method for pathfinding
     * @param n node
     * @param n1 other node
     * @return true if route can be created
     */
    public boolean routeExists(Node n, Node n1) {
        return getRoute(n, n1) != null;
    }

    /**
     * Method for finding if edge between two nodes is closed
     * @param n first node
     * @param n1 second node
     * @return is edge closed
     */
    public boolean isEdgeClosed(Node n, Node n1) {
        for(Pair<Node, Node> closure: closures) {
            if (closure.getX().equals(n) && closure.getY().equals(n1) || closure.getX().equals(n1) && closure.getY().equals(n)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method for finding street by nodes
     * @param n first node
     * @param n1 second node
     * @return street, if nodes aren't on one street returns null
     */
    public Street getStreetByNodes(Node n, Node n1) {
        for (Street s: streets) {
            List<Node> nodes = s.listNodes();
            if (nodes.contains(n) && nodes.contains(n1)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Computes total route distance
     * @param route route to compute
     * @return total distance
     */
    public double getRouteDistance(List<Node> route) {
        double distance = 0;
        for (int i = 0; i + 1 < route.size(); i++) {
            distance += route.get(i).getCost(route.get(i + 1));
        }
        return distance;
    }

    /**
     * Returns segment of street between crossroads
     * @param e start edge
     * @return segment
     */
    public List<Edge> getSegmentFromEdge(Edge e) {
        // find street
        List<Edge> edges = new ArrayList<>();

        edges.add(e);

        for (Street s: streets) {
            List<Edge> streetEdges = s.getEdges();
            if (streetEdges.contains(e)) {
                int index = streetEdges.indexOf(e);
                // search backward
                for (int i = index - 1; i >=0; i--) {
                    if (streetEdges.get(i).end.neighbours(this).size() <= 2) {
                        edges.add(streetEdges.get(i));
                        continue;
                    }
                    break;
                }
                // search forward
                for (int i = index + 1; i < streetEdges.size(); i++) {
                    if (streetEdges.get(i).start.neighbours(this).size() <= 2) {
                        edges.add(streetEdges.get(i));
                        continue;
                    }
                    break;
                }

                edges.sort(Comparator.comparingInt(streetEdges::indexOf));
            }
        }

        return edges;
    }

    /**
     * Returns if segment is closed and has detour
     * @param segment segment to check
     * @return closed
     */
    public boolean isSegmentClosed(List<Edge> segment) {
        List<Node> nodes = new ArrayList<>();
        for (Edge e: segment) {
            nodes.add(e.start);
        }
        nodes.add(segment.get(segment.size() - 1).end);

        for(Pair<List<Node>, List<Node>> detour: detours) {
            if (detour.getX().equals(nodes)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update method for vehicles and lines
     * @param delta delta time since last update
     */
    public void onTick(Duration delta) {
        for (Line l: lines) {
            l.tick(delta, this);
        }
        for (Vehicle v: vehicles) {
            v.move(delta);
        }
    }

    public void addDetour(Pair<List<Edge>, List<Edge>> listObjectPair) {
        List<Edge> edges = listObjectPair.getX();

        List<Node> nodes = new ArrayList<>();
        for (Edge e: edges) {
            nodes.add(e.start);
        }
        nodes.add(edges.get(edges.size() - 1).end);

        /*List<Edge> detourEdges = listObjectPair.getY();

        List<Node> detour = new ArrayList<>();
        for (Edge e: detourEdges) {
            nodes.add(e.start);
        }
        detour.add(detourEdges.get(detourEdges.size() - 1).end);*/

        detours.add(new Pair<>(nodes, null));
    }
}
