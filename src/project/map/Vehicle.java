package project.map;

import java.time.Duration;
import java.util.List;

public class Vehicle {
    private Line line;
    private Map map;
    private double x;
    private double y;
    private Node currentTarget;
    private Node lastStop;
    private Street currentStreet;

    //stops
    private long timeOnStopLeft = 0;
    private boolean waiting = false;

    private Duration delay = Duration.ZERO;

    private static final double maxSpeed = 0.02;

    public Vehicle(Line line, Map map) {
        this.line = line;
        this.map = map;

        resetVehicle();
        map.vehicles.add(this);
    }

    public void move(Duration delta) {
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

                recomputeRoute(true);
            }
        }
    }

    public void recomputeRoute(boolean isOnTarget) {
        List<Node> route = line.anyRoute(map, currentTarget,lastStop);
        if (route == null) {
            return;
        }
        if (isOnTarget) {
            currentTarget = route.get(1);
            currentStreet = map.getStreetByNodes(route.get(0), route.get(1));
        } else {
            currentTarget = route.get(0);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void resetVehicle() {
        Node start = line.getNodes().get(0);
        lastStop = start;
        currentTarget = start;
        x = start.x;
        y = start.y;
        recomputeRoute(true);
    }
}
