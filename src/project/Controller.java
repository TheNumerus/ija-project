package project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import project.map.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Controller {
    public Label TextLabel;
    public Pane MapPane;
    public Group MapTransform;

    public Map map = new Map();
    public Image busStopIcon = new Image(Controller.class.getResourceAsStream("busStop.png"));

    private Node DragStart = new Node(0.0, 0.0, null);
    private Node OrigTransform = new Node(0.0, 0.0, null);

    public void initialize() {
        // TODO replace by reset only, map will be loaded from elsewhere
        //loadMap(Map.placeholderData());
    }

    private void loadMap(Map map) {
        MapTransform.getChildren().clear();
        resetView(null);
        MapTransform.getChildren().clear();
        for (Street s: map.streets) {
            // TODO check for duplicate edges already on map
            for (Edge e: s.getEdges()) {
                EdgeLine el = new EdgeLine(e, s);
                MapTransform.getChildren().add(el);

                if(e.start.stop != null){
                    /*ImageView busStop = new ImageView(busStopIcon);
                    busStop.setFitHeight(20);
                    busStop.setFitWidth(20);

                    busStop.setX(e.start.x - 10);
                    busStop.setY(e.start.y - 10);*/
                    StopImg busStop = new StopImg(e, true);
                    MapTransform.getChildren().add(busStop);
                }
                if(e.end.stop != null){
                    /*ImageView busStop = new ImageView(busStopIcon);
                    busStop.setFitHeight(20);
                    busStop.setFitWidth(20);

                    busStop.setTranslateX(e.end.x - 10);
                    busStop.setTranslateY(e.end.y - 10);*/
                    StopImg busStop = new StopImg(e, false);
                    MapTransform.getChildren().add(busStop);
                }
            }
        }
    }

    public void loadDataButtonClick(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open resource file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Map file", "*.json")
        );

        Map map = new Map();

        File file = fileChooser.showOpenDialog(null);
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

        Gson gson = builder.create();
        map = gson.fromJson(jsonString, Map.class);
        loadMap(map);
    }

    public void onMousePressed(MouseEvent event) {
        DragStart.x = event.getSceneX();
        DragStart.y = event.getSceneY();
        OrigTransform.x = MapTransform.getTranslateX();
        OrigTransform.y = MapTransform.getTranslateY();
    }

    public void onMouseDragged(MouseEvent event) {
        double delta_x = event.getSceneX() - DragStart.x;
        double delta_y = event.getSceneY() - DragStart.y;
        MapTransform.setTranslateX(delta_x + OrigTransform.x);
        MapTransform.setTranslateY(delta_y + OrigTransform.y);
        event.consume();
    }

    public void onScroll(ScrollEvent event) {
        double val = event.getDeltaY() / 400.0;
        double new_zoom = Double.max(MapTransform.getScaleX() + val, 0.1);
        MapTransform.setScaleX(new_zoom);
        MapTransform.setScaleY(new_zoom);
    }

    public void resetView(ActionEvent actionEvent) {
        MapTransform.setTranslateX(640);
        MapTransform.setTranslateY(360);
        MapTransform.setScaleX(1);
        MapTransform.setScaleY(1);

    }
}