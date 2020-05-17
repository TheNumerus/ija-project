package project.map;

import project.Pair;

import java.time.Duration;
import java.util.List;

public class RouteData {
    public List<Pair<Node, Duration>> defaultRoute;
    public Node nextStop;
    public Duration currentRouteTime;
    public List<Node> skippedStops;

    public RouteData() {
    }

    public RouteData(List<Pair<Node, Duration>> defaultRoute, Node nextStop, Duration currentRouteTime, List<Node> skippedStops) {
        this.defaultRoute = defaultRoute;
        this.nextStop = nextStop;
        this.currentRouteTime = currentRouteTime;
        this.skippedStops = skippedStops;
    }
}
