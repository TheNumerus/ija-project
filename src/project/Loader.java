package project;

import com.google.gson.*;
import project.map.Map;
import project.map.Node;
import project.map.Street;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

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
        //TODO catch syntax error
        try {
            JsonObject e = gson.fromJson(jsonString, JsonObject.class);
            JsonArray streets = e.getAsJsonArray("streets");
            for (JsonElement jsonElement : streets) {
                JsonObject street = jsonElement.getAsJsonObject();
                JsonArray nodes = street.get("nodes").getAsJsonArray();
                ArrayList<Node> nodesArr = new ArrayList<Node>();

                for (JsonElement node : nodes) {
                    Node n = new Node(node.getAsJsonObject().get("x").getAsDouble(), node.getAsJsonObject().get("y").getAsDouble());
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
        } catch (JsonSyntaxException e) {
            return null;
        }

        return map;
    }
}
