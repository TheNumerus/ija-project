package project.map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Line {
    public int number;
    public double delay;
    private List<Node> stops;

    public Line(int number, double delay, List<Node> stops) {
        this.delay = delay;
        this.number = number;
        this.stops = stops;
    }

    public List<Stop> getStops() {
        return stops.stream().map(s -> s.stop).collect(Collectors.toList());
    }

    public List<Node> findRoute(Map m) {
        List<Node> route = new ArrayList<>();
        for (int i = 0; i < stops.size() - 1; i++) {
            List<Node> part = m.getRoute(stops.get(i), stops.get(i + 1));
            route.addAll(part.subList(0, part.size() - 1));
        }
        route.add(stops.get(stops.size() - 1));
        return route;
    }
}
