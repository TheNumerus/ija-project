package project.map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Line {
    public int number;
    public double delay;
    private final List<Node> stops;
    private List<Node> currentRoute;

    public Line(int number, double delay, List<Node> stops, Map m) {
        this.delay = delay;
        this.number = number;
        this.stops = stops;
        currentRoute = findRoute(m);
    }

    public List<Stop> getStops() {
        return stops.stream().map(s -> s.stop).collect(Collectors.toList());
    }

    public List<Node> getNodes() {
        return stops;
    }

    public List<Node> findRoute(Map m) {
        List<Node> route = new ArrayList<>();
        for (int i = 0; i < stops.size() - 1; i++) {
            List<Node> part = m.getRoute(stops.get(i), stops.get(i + 1));
            // TODO
            if (part == null) {
                return null;
            }
            route.addAll(part.subList(0, part.size() - 1));
        }
        route.add(stops.get(stops.size() - 1));
        return route;
    }

    public List<Node> getCurrentRoute() {
        return currentRoute;
    }

}
