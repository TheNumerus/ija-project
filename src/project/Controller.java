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
import project.map.Map;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    @FXML
    private Label time;
    public Pane MapPane;
    public Group MapTransform;
    public Button speedUp_button;
    public Button slowDown_button;
    public Button playPause_button;
    public Label speed;

    public Map map;

    private final Pair<Double, Double> DragStart = new Pair<>(0.0, 0.0);
    private final Pair<Double, Double> OrigTransform = new Pair<>(0.0, 0.0);

    private final List<EdgeRoute> routeEdges = new ArrayList<>();
    private final List<StopImg> stops = new ArrayList<>();
    private final List<VehicleUI> vehicles = new ArrayList<>();

    private InternalClock clock;

    public void initialize() {
        clock = new InternalClock(this::tick);
        clock.setPaused(true);
    }

    public void close() {
        clock.cancel();
    }

    public void tick(Duration time, Duration delta) {
        setTime(time);
        map.onTick(delta);
        renderVehicles();
    }

    private void renderVehicles() {
        for (Vehicle v: map.vehicles) {
            List<VehicleUI> filtered = vehicles.stream().filter((vui) -> vui.getVehicle().equals(v)).collect(Collectors.toList());
            if (filtered.size() > 0) {
                filtered.get(0).newPos();
            } else {
                VehicleUI vui = new VehicleUI(v);
                MapTransform.getChildren().add(vui);
                vehicles.add(vui);
                vui.newPos();
            }
        }
    }

    private void loadMap() {
        MapTransform.getChildren().clear();
        resetView(null);
        for (Street s: map.streets) {
            for (Edge e: s.getEdges()) {
                EdgeLine el = new EdgeLine(e, s, (closed) -> {
                    if (closed) {
                        map.closures.add(new Pair<>(e.start, e.end));
                    } else {
                        map.closures.remove(new Pair<>(e.start, e.end));
                    }
                    map.recomputeRoutes();
                    //renderLines();
                });
                MapTransform.getChildren().add(el);

                if(e.start.stop != null){
                    StopImg busStop = new StopImg(e.start, this);
                    MapTransform.getChildren().add(busStop);
                    busStop.setPos();
                    this.stops.add(busStop);
                }
            }
        }
        //renderLines();
        clock.setPaused(false);
        playPause_button.setDisable(false);
    }

    private void renderLines() {
        clearRoute();
        for(Line l: map.lines) {
            Color clr = new Color((l.number * 47) % 255, (l.number * 7) % 255, (l.number * 11) % 255);
            showRoute(l.findRoute(map), clr);
        }
    }

    public void clearRoute(){
        for (EdgeRoute routeEdge : this.routeEdges) {
            MapTransform.getChildren().remove(routeEdge);
        }
        this.routeEdges.clear();
    }

    public void clearRoute(List<EdgeRoute> routes){
        for(EdgeRoute line : routes){
            /*if(MapTransform.getChildren().contains(route)){
                MapTransform.getChildren().remove(route);
            }*/
            if(MapTransform.getChildren().contains(line)){
                MapTransform.getChildren().remove(line);
            }


        }
    }

    public List<EdgeRoute> showRoute(List<Node> nodes){
        Color defaultColor = new Color(255, 213, 3);
        List<EdgeRoute> routes = showRoute(nodes, defaultColor);
        return routes;
    }

    public List<EdgeRoute> showRoute(List<Node> nodes, Color color){
        if (nodes == null) {
            return null;
        }
        // generating hex string
        String hexColor = Integer.toHexString(color.getRGB() & 0xffffff);
        if(hexColor.length() < 6){
            hexColor = "0" + hexColor;
        }
        hexColor = "#" + hexColor;

        if(nodes.isEmpty()){
            return null;
        }

        List<EdgeRoute> routes = new ArrayList<EdgeRoute>();

        //deleting stops
        for(StopImg stop : this.stops){
            MapTransform.getChildren().remove(stop);
        }

        //adding route
        for (int i = 1; i < nodes.size(); i++){
            EdgeRoute e = new EdgeRoute(nodes.get(i - 1), nodes.get(i), hexColor);
            e.mouseTransparentProperty().setValue(true);
            MapTransform.getChildren().add(e);
            this.routeEdges.add(e);
            routes.add(e);
        }

        //adding stops back
        for(StopImg stop : this.stops){
            MapTransform.getChildren().add(stop);
        }
        return routes;
    }

    public void loadDataButtonClick(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open resource file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Map file", "*.json")
        );

        File file = fileChooser.showOpenDialog(((Button)actionEvent.getTarget()).getScene().getWindow());
        if (file != null) {
            map = Loader.LoadMap(file);
            if (map == null) {
                // show popup or something
                Alert a = new Alert(Alert.AlertType.ERROR, "File contents not valid map.");
                a.showAndWait();
                return;
            }

            loadMap();
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

    public void speedUp(ActionEvent actionEvent){
        double currentSpeed = clock.speedUp();
        slowDown_button.setDisable(false);
        if(currentSpeed == 10.0){
            speedUp_button.setDisable(true);
        }
        speed.setText(currentSpeed + "x");
    }

    public void slowDown(ActionEvent actionEvent){
        double currentSpeed = clock.speedDown();
        speedUp_button.setDisable(false);
        if(currentSpeed == 0.1){
            slowDown_button.setDisable(true);
        }
        speed.setText(currentSpeed + "x");
    }

    public void resetSpeed(ActionEvent actionEvent){
        clock.resetSpeed();
        speedUp_button.setDisable(false);
        slowDown_button.setDisable(false);
        speed.setText("1.0x");
    }

    public void playPause(ActionEvent actionEvent){
        if(clock.isPaused()){
            clock.setPaused(false);
            playPause_button.setText("Pause");
        }
        else{
            clock.setPaused(true);
            playPause_button.setText("Play");
        }
    }

    public void onMousePressed(MouseEvent event) {
        DragStart.setX(event.getSceneX());
        DragStart.setY(event.getSceneY());
        OrigTransform.setX(MapTransform.getTranslateX());
        OrigTransform.setY(MapTransform.getTranslateY());
    }

    public void onMouseDragged(MouseEvent event) {
        double delta_x = event.getSceneX() - DragStart.getX();
        double delta_y = event.getSceneY() - DragStart.getY();
        MapTransform.setTranslateX(delta_x + OrigTransform.getX());
        MapTransform.setTranslateY(delta_y + OrigTransform.getY());
        event.consume();
    }

    public void onScroll(ScrollEvent event) {
        double val = event.getDeltaY() / 400.0;
        double new_zoom = Double.max(MapTransform.getScaleX() + val, 0.1);
        MapTransform.setScaleX(new_zoom);
        MapTransform.setScaleY(new_zoom);
    }

    public void onMouseClicked(MouseEvent event){
        if(event.getTarget().equals(this.MapPane)) {
            clearRoute();
        }
    }

    public void resetView(ActionEvent actionEvent) {
        MapTransform.setTranslateX(640);
        MapTransform.setTranslateY(360);
        MapTransform.setScaleX(1);
        MapTransform.setScaleY(1);

    }
}