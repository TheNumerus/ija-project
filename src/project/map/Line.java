package project.map;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Line {
    public int number;
    public double delay;
    public final List<Node> stops;
    private List<Node> currentRoute;
    private int lastVehicleSend = 0;
    private int vehicleCount = 0;

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
        Node start = stops.get(0);
        Node end = stops.get(1);

        List<Node> route = new ArrayList<>();
        while(!start.equals(stops.get(stops.size() - 1))) {
            List<Node> part = m.getRoute(start, end);
            if (part == null) {
                // is last
                if (end.equals(stops.get(stops.size() - 1))) {
                    return null;
                }
                end = stops.get(stops.indexOf(end) + 1);
            } else {
                // is last
                if (end.equals(stops.get(stops.size() - 1))) {
                    route.addAll(part);
                    return route;
                } else {
                    start = end;
                    end = stops.get(stops.indexOf(end) + 1);
                    route.addAll(part.subList(0, part.size() - 1));
                }
            }
        }

        return route;
    }

    public List<Node> anyRoute(Map m, Node current, Node lastStop) {
        Node start = current;
        Node end = stops.get(stops.indexOf(lastStop) + 1);

        List<Node> route = new ArrayList<>();
        while(!start.equals(stops.get(stops.size() - 1))) {
            List<Node> part = m.getRoute(start, end);
            if (part == null) {
                // is last
                if (end.equals(stops.get(stops.size() - 1))) {
                    return null;
                }
                end = stops.get(stops.indexOf(end) + 1);
            } else {
                // is last
                if (end.equals(stops.get(stops.size() - 1))) {
                    route.addAll(part);
                    return route;
                } else {
                    start = end;
                    end = stops.get(stops.indexOf(end) + 1);
                    route.addAll(part.subList(0, part.size() - 1));
                }
            }
        }

        return route;
    }

    public List<Node> getCurrentRoute() {
        return currentRoute;
    }

    public boolean isNextStop(Node current, Node next) {
        return stops.indexOf(next) - stops.indexOf(current) == 1;
    }

    public void tick(Duration delta, Map m) {
        if (vehicleCount >= 10) {
            return;
        }

        long milis = delta.toMillis();
        long mili_delay = (long)(delay * 1000);

        while (milis > 0) {
            if ((milis + lastVehicleSend) >= mili_delay) {
                long rest = (lastVehicleSend + milis) - mili_delay;
                new Vehicle(this, m);
                lastVehicleSend = 0;
                vehicleCount++;
                if (vehicleCount >= 10) {
                    return;
                }
                milis -= rest;
            } else {
                lastVehicleSend += milis;
                milis = 0;
            }
        }
    }
}
