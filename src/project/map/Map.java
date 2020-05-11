package project.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Map {
    public List<Street> streets;
    public List<Stop> stops;
    public List<Line> lines;

    public Map() {
        streets = new ArrayList<>();
        stops = new ArrayList<>();
        lines = new ArrayList<>();
    }
}
