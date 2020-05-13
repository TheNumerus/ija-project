package project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;

import project.map.*;
import project.Loader;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.awt.Color;

public class Controller {
    @FXML
    private Label time;
    public Label TextLabel;
    public Pane MapPane;
    public Group MapTransform;

    public Map map = new Map();
    public Image busStopIcon = new Image(Controller.class.getResourceAsStream("busStop.png"));

    // TODO replace by something else than node
    private Node DragStart = new Node(0.0, 0.0, null);
    private Node OrigTransform = new Node(0.0, 0.0, null);

    private List<EdgeRoute> routeEdges = new ArrayList<EdgeRoute>();
    private List<StopImg> stops = new ArrayList<StopImg>();

    public void initialize() {
        // TODO replace by reset only, map will be loaded from elsewhere
        //loadMap(Map.placeholderData());
    }

    private void loadMap(Map map) {
        MapTransform.getChildren().clear();
        resetView(null);
        for (Street s: map.streets) {
            for (Edge e: s.getEdges()) {
                EdgeLine el = new EdgeLine(e, s);
                MapTransform.getChildren().add(el);

                if(e.start.stop != null){
                    StopImg busStop = new StopImg(e.start);
                    MapTransform.getChildren().add(busStop);
                    busStop.setPos();
                    this.stops.add(busStop);
                }
            }
        }
        for(Line l: map.lines) {
            showRoute(l.findRoute(map));
        }
        //showRoute(map.getRoute(map.streets.get(0).listNodes().get(2), map.streets.get(2).listNodes().get(6)));
    }

    private void clearRoute(){
        for (EdgeRoute routeEdge : this.routeEdges) {
            if(MapTransform.getChildren().contains(routeEdge)){
                MapTransform.getChildren().remove(routeEdge);
            }
        }
        this.routeEdges.clear();
    }

    private void showRoute(List<Node> nodes){
        Color defaultColor = new Color(255, 213, 3);
        showRoute(nodes, defaultColor);
    }

    private void showRoute(List<Node> nodes, Color color){
        // generating hex string
        String hexColor = Integer.toHexString(color.getRGB() & 0xffffff);
        if(hexColor.length() < 6){
            hexColor = "0" + hexColor;
        }
        hexColor = "#" + hexColor;

        if(nodes.isEmpty()){
            return;
        }

        //deleting stops
        for(StopImg stop : this.stops){
            if(MapTransform.getChildren().contains(stop)){
                MapTransform.getChildren().remove(stop);
            }
        }

        //adding route
        for (int i = 1; i < nodes.size(); i++){
            EdgeRoute e = new EdgeRoute(nodes.get(i - 1), nodes.get(i), hexColor);
            e.mouseTransparentProperty().setValue(true);
            MapTransform.getChildren().add(e);
            this.routeEdges.add(e);
        }

        //adding stops back
        for(StopImg stop : this.stops){
            MapTransform.getChildren().add(stop);
        }
    }

    public void loadDataButtonClick(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open resource file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Map file", "*.json")
        );

        File file = fileChooser.showOpenDialog(((Button)actionEvent.getTarget()).getScene().getWindow());
        if (file != null) {
            Map map = Loader.LoadMap(file);
            if (map == null) {
                // show popup or something
                Alert a = new Alert(Alert.AlertType.ERROR, "File contents not valid map.");
                a.showAndWait();
                return;
            }

            loadMap(map);
        }
    }

    public void setTime(Duration time){
        long totalSecs = time.getSeconds();
        String hours = Integer.toString((int)totalSecs / 3600);
        String minutes = Integer.toString((int)(totalSecs % 3600) / 60);
        String seconds = Integer.toString((int)totalSecs % 60);

        if(hours.length() == 1) hours = "0" + hours;
        if(minutes.length() == 1) minutes = "0" + minutes;
        if(seconds.length() == 1) seconds = "0" + seconds;

        this.time.setText(hours + ":" + minutes + ":" + seconds);
    }

    public void resetTime(){
        this.time.setText("00:00:00");
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