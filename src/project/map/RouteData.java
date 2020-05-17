package project.map;

import project.Pair;

import java.time.Duration;
import java.util.List;

public class RouteData {
    public List<Pair<Node, Duration>> defaultRoute;
    public Node nextStop;
    public Duration currentRouteTime;
    public List<Node> skippedStops;
}
