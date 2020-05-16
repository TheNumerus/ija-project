package project.map;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data class for vehicles
 */
public class Vehicle {
    private final Line line;
    private final Map map;
    private double x;
    private double y;
    private Node currentTarget;
    private Node lastStop;
    private Street currentStreet;

    //stops
    private long timeOnStopLeft = 0;
    private boolean waiting = false;

    private boolean stopped = false;

    private Duration delay = Duration.ZERO;

    private static final double maxSpeed = 0.02;

    /**
     * Vehicle constructor
     *
     * Vehicle will start on first stop on provided line
     * @param line line
     * @param map map data
     */
    public Vehicle(Line line, Map map) {
        this.line = line;
        this.map = map;

        resetVehicle();
        map.vehicles.add(this);
    }

    /**
     * Tick function for bus simulation
     * @param delta time since last update
     */
    public void move(Duration delta) {
        //check if any route can be made
        if (stopped) {
            if (!recomputeRoute(true)) {
                stopped = false;
            }
        }

        long milis = delta.toMillis();
        while(milis != 0) {
            if (waiting) {
                //on stop
                if(timeOnStopLeft > milis) {
                    timeOnStopLeft -= milis;
                    return;
                } else {
                    milis -= timeOnStopLeft;
                    waiting = false;
                    continue;
                }
            }
            double delta_x = currentTarget.x - x;
            double delta_y = currentTarget.y - y;
            double distance_to_next_node = Math.sqrt(Math.pow(delta_x, 2.0) + Math.pow(delta_y, 2.0));

            double speed = maxSpeed * currentStreet.costMultiplier;

            double time_to_next_node = distance_to_next_node / speed;
            if (time_to_next_node > milis) {
                double travel_dist = milis * speed;
                x += delta_x * travel_dist / distance_to_next_node;
                y += delta_y * travel_dist / distance_to_next_node;
                return;
            } else {
                milis -= time_to_next_node;
                x = currentTarget.x;
                y = currentTarget.y;
                // finished route
                if ((line.getNodes().indexOf(currentTarget) + 1) == line.getNodes().size()) {
                    resetVehicle();
                    continue;
                }

                if (line.isNextStop(lastStop,currentTarget)) {
                    lastStop = currentTarget;
                    timeOnStopLeft = 5000;
                    waiting = true;
                    continue;
                }

                // should stop
                if (recomputeRoute(true)) {
                    stopped = true;
                    return;
                }
            }
        }
    }

    private boolean recomputeRoute(boolean isOnTarget) {
        List<Node> route = line.anyRoute(map, currentTarget,lastStop);
        if (route == null) {
            return true;
        }
        if (isOnTarget) {
            currentTarget = route.get(1);
            currentStreet = map.getStreetByNodes(route.get(0), route.get(1));
        } else {
            currentTarget = route.get(0);
        }
        return false;
    }

    /**
     * Returns x coordinate of vehicle
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Returns y coordiante of vehicle
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Resets vehicle
     */
    public void resetVehicle() {
        Node start = line.getNodes().get(0);
        lastStop = start;
        currentTarget = start;
        x = start.x;
        y = start.y;
        stopped = false;
        currentStreet = map.streets.stream().filter( (s) -> s.listNodes().contains(start)).collect(Collectors.toList()).get(0);
        recomputeRoute(true);
    }
}
