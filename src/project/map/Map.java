package project.map;

import java.util.ArrayList;
import java.util.List;

public class Map {
    public List<Street> streets;

    public Map() {
        streets = new ArrayList<>();
    }

    public static Map placeholderData() {
        Map map = new Map();
        Node n1 = new Node(0.0, 0.0);
        Node n2 = new Node(100.0, 100.0);
        Node n3 = new Node(200.0, 100.0);
        Edge e1 = new Edge(n1, n2, 1, false);
        Edge e2 = new Edge(n2, n3, 1, false);
        Street s = new Street(e1, "ULICE");
        s.addEdge(e2);
        map.streets.add(s);
        return map;
    }
}
