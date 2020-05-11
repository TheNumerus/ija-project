package project.map;

import java.util.List;

public class Line {
    public int number;
    public double delay;
    private List<Stop> stops;

    public Line(int number, double delay, List<Stop> stops) {
        this.delay = delay;
        this.number = number;
        this.stops = stops;
    }

    public List<Stop> getStops() {
        return stops;
    }
}
