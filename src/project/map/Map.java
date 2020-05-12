package project.map;

import project.Pair;

import java.util.*;

public class Map {
    public List<Street> streets;
    public List<Stop> stops;
    public List<Line> lines;

    public Map() {
        streets = new ArrayList<>();
        stops = new ArrayList<>();
        lines = new ArrayList<>();
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
        }
        route.add(start);

        System.out.println(cameFrom);
        return route;
    }
}
