package project.map;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data class for bus lines
 */
public class Line {
    /**
     * Line number, used mainly for color coding lines
     */
    public int number;
    /**
     * Delay between two buses, in seconds
     */
    public double delay;
    private final List<Node> stops;
    private final List<Node> defaultRoute;
    private int lastVehicleSend = 0;
    private int vehicleCount = 0;

    public Line(int number, double delay, List<Node> stops, Map m) {
        this.delay = delay;
        this.number = number;
        this.stops = stops;
        defaultRoute = findRoute(m);
    }

    /**
     * Returns list of all stops in this line
     * @return list of stops
     */
    public List<Stop> getStops() {
        return stops.stream().map(s -> s.stop).collect(Collectors.toList());
    }

    /**
     * Returns list of all nodes containing stops
     * @return list of nodes
     */
    public List<Node> getNodes() {
        return stops;
    }

    /**
     * Tries to find route beteween first and last stop
     * @param m map with node info
     * @return list with route nodes, null if route cannot be found
     */
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

    /**
     * Tries to find any route, even if final stop cannot be reached
     * @param m map info
     * @param current starting node
     * @param lastStop last stop which was reached
     * @return rotue, null if nothing could be found
     */
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

    /**
     * Returns default route, with no closures applied
     * @return route
     */
    public List<Node> getDefaultRoute() {
        return defaultRoute;
    }

    /**
     * Checks if two stops are in sequence
     * @param current first stop
     * @param next second stop
     * @return result
     */
    public boolean isNextStop(Node current, Node next) {
        return stops.indexOf(next) - stops.indexOf(current) == 1;
    }

    /**
     * Tick function for sending vehicles
     * @param delta time delta
     * @param m map data
     */
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
