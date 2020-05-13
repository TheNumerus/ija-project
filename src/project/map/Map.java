package project.map;

import project.Pair;

import java.util.*;

public class Map {
    public List<Street> streets;
    public List<Node> stops;
    public List<Line> lines;
    public List<Pair<Node, Node>> closures;

    public Map() {
        streets = new ArrayList<>();
        stops = new ArrayList<>();
        lines = new ArrayList<>();
        closures = new ArrayList<>();
    }

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

    public void recomputeRoutes() {
        for (Line l: lines) {
            l.findRoute(this);
        }
    }

    public boolean isEdgeClosed(Node n, Node n1) {
        for(Pair<Node, Node> closure: closures) {
            if (closure.getX().equals(n) && closure.getY().equals(n1) || closure.getX().equals(n1) && closure.getY().equals(n)) {
                return true;
            }
        }
        return false;
    }
}
