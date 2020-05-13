package project.map;

import java.time.Duration;

public class Vehicle {
    private Line line;
    private double x;
    private double y;

    private static final double maxSpeed = 10.0;

    public Vehicle(Line line) {
        this.line = line;

        Node start = line.getNodes().get(0);
        x = start.x;
        y = start.y;
    }

    public void move(Duration delta) {
        double distance = (double)(delta.getNano() / 1000);
        //TODO
    }

    public void recomputeRoute() {

    }
}
