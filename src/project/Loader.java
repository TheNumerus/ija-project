package project;

import com.google.gson.*;
import project.map.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Loader {
    public static Map LoadMap(File file) {
        String jsonString = "";
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                jsonString += scanner.nextLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Map map = new Map();

        Gson gson = builder.create();
        try {
            JsonObject e = gson.fromJson(jsonString, JsonObject.class);
            JsonArray streets = e.getAsJsonArray("streets");
            for (JsonElement jsonElement : streets) {
                JsonObject street = jsonElement.getAsJsonObject();
                JsonArray nodes = street.get("nodes").getAsJsonArray();
                ArrayList<Node> nodesArr = new ArrayList<>();

                for (JsonElement node : nodes) {
                    Node n = new Node(node.getAsJsonObject().get("x").getAsDouble(), node.getAsJsonObject().get("y").getAsDouble());

                    //deduplicate
                    for(Street s: map.streets) {
                        for(Node nod: s.listNodes()) {
                            if (nod.equals(n)) {
                                n = nod;
                            }
                        }
                    }

                    if (node.getAsJsonObject().has("stop")) {
                        n.stop = new Stop(node.getAsJsonObject().get("stop").getAsJsonObject().get("name").getAsString());
                        map.stops.add(n);
                    }
                    nodesArr.add(n);
                }

                Street s = new Street(street.get("name").getAsString(), nodesArr);
                s.costMultiplier = street.get("costMultiplier").getAsDouble();

                boolean oneWay = false;
                if (street.has("oneWay")) {
                    oneWay = street.get("oneWay").getAsBoolean();
                }

                s.setOneWay(oneWay);

                map.streets.add(s);
            }

            JsonArray lines = e.getAsJsonArray("lines");
            for (JsonElement ll: lines) {
                JsonObject l = ll.getAsJsonObject();
                int num = l.get("number").getAsInt();
                double delay = l.get("delay").getAsDouble();

                List<Node> stopList = new ArrayList<>();
                JsonArray stops = l.get("stops").getAsJsonArray();
                for(JsonElement s: stops) {
                    String stopname = s.getAsString();
                    if (map.stops.stream().anyMatch(stop -> stop.stop.name.equals(stopname))) {
                        List<Node> filtered = map.stops.stream().filter(stop -> stop.stop.name.equals(stopname)).collect(Collectors.toList());
                        stopList.add(filtered.get(0));
                    } else {
                        return null;
                    }
                }
                map.lines.add(new Line(num, delay, stopList, map));
            }
        } catch (JsonSyntaxException | NullPointerException e) {
            return null;
        }

        return map;
    }
}
