package project.map;

import project.Pair;

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

    private List<Pair<Node, Double>> defaultDistanceData;


    /**
     * constructor of Line
     * @param number number of this line
     * @param delay delay value between two buses
     * @param stops liset of stops on this line
     * @param m parent map
     */
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
     * Tries to find route beteween first and last stop.
     * Should only be used on start for computing default route.
     * @param m map with node info
     * @return list with route nodes, null if route cannot be found
     */
    private List<Node> findRoute(Map m) {
        Node start = stops.get(0);
        Node end = stops.get(1);

        defaultDistanceData = new ArrayList<>();

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
                double part_distance = m.getRouteDistance(part);
                // is last
                if (end.equals(stops.get(stops.size() - 1))) {
                    route.addAll(part);
                    defaultDistanceData.add(new Pair<>(end, part_distance));
                    return route;
                } else {
                    defaultDistanceData.add(new Pair<>(start, part_distance));
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
     * @param stopsSkipped in-out list with skipped stops
     * @return pair of route and next stop, null if nothing could be found or already in finish
     */
    public Pair<List<Node>, Node> anyRoute(Map m, Node current, Node lastStop, List<Node> stopsSkipped) {
        Node lastStopOnLine = stops.get(stops.size() - 1);

        Node start = current;

        // alraedy in finish
        if (lastStop.equals(lastStopOnLine)) {
            return null;
        }

        Node end = stops.get(stops.indexOf(lastStop) + 1);

        // already in finish
        if (start == end) {
            return null;
        }

        // first find if route can be found to any of stops ahead
        List<Node> route = new ArrayList<>();

        Node furthestStop = null;

        for(Node s: stops) {
            if(m.routeExists(start, s)) {
                furthestStop = s;
            }
        }

        // no route can be made, stop vehicle
        if (furthestStop == null) {
            return null;
        }

        if (start.equals(furthestStop)) {
            return null;
        }

        // go back?
        if (stops.indexOf(lastStop) >= stops.indexOf(furthestStop)) {
            return new Pair<>(m.getRoute(start, furthestStop), furthestStop);
        }

        Node nextStop = end;

        // now go to furthest stop we can
        do {
            // advance one stop
            List<Node> part = m.getRoute(start, end);
            if (part == null) {
                // return what we got so far
                if (end.equals(lastStopOnLine)) {
                    return new Pair<>(route, nextStop);
                }
                //skip one stop
                stopsSkipped.add(end);
                end = stops.get(stops.indexOf(end) + 1);
                nextStop = end;
            } else {
                if (end.equals(lastStopOnLine)) {
                    // is last
                    route.addAll(part);
                    return new Pair<>(route, nextStop);
                } else {
                    // advance one stop
                    start = end;
                    end = stops.get(stops.indexOf(end) + 1);
                    route.addAll(part);
                }
            }
        } while (!start.equals(lastStopOnLine));

        return new Pair<>(route, nextStop);
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

    /**
     * returns line of this line
     * @return number of this line
     */
    public int getNumber(){return this.number;}

    public List<Pair<Node, Duration>> getDefaultTimeData(double speed) {
        List<Pair<Node, Duration>> timeData = new ArrayList<>();
        for(Pair<Node, Double>  stop: defaultDistanceData) {
            Duration time = Duration.ofMillis((long)(stop.getY() * speed) + Math.max(defaultDistanceData.indexOf(stop) - 1, 0) * 5000);
            Pair<Node, Duration> timeStop = new Pair<>(stop.getX(), time);
            timeData.add(timeStop);
        }

        return timeData;
    }
}
