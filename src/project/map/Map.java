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

    public Map() {
        streets = new ArrayList<>();
    }
}
