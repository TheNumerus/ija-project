/*
soubor: project.gui.VehicleUI.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: soubor je třída, ovládajicí prvky vytvořené v souboru VehicleUI.fxml
        nastavuje pozici kroužku, reprezentujícího vozidlo a stará se o označování tohoto vozidla
 */

package project.gui;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import project.Controller;
import project.EditMode;
import project.Loader;
import project.Pair;
import project.map.Node;
import project.map.RouteData;
import project.map.Vehicle;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing vehicle on map
 */
public class VehicleUI extends Circle {
    private final MapPane mapPane;
    private final Controller controller;
    private final Vehicle vehicle;
    private boolean selected;
    private final ChangeListener<List<Node>> changeListener;

    @FXML
    Circle circle;

    /**
     * constructor of vehicle
     * @param v vehicle, that is represented
     * @param c controller data
     */
    public VehicleUI(Vehicle v, Controller c, MapPane m) {
        Loader.loadFXMLDef(getClass().getResource("VehicleUI.fxml"), this);

        setRadius(10.0);
        vehicle = v;
        controller = c;
        mapPane = m;

        selected = false;
        this.changeListener = (observable, oldValue, newValue) -> showRoute(newValue);
    }

    /**
     * returns vehicle, that is represented
     * @return vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * sets new position
     */
    public void newPos() {
        setTranslateX(vehicle.getX());
        setTranslateY(vehicle.getY());
    }

    @FXML
    private void mouseClicked(MouseEvent mouseEvent){
        if(controller.currentMode == EditMode.CLOSURES){
            if(selected) {
                controller.busUnClicked();
            }
            else{
                controller.busClicked(vehicle, this);
            }
        }
    }

    /**
     * selects bus
     */
    public void select(){
        circle.setStrokeWidth(2);
        circle.setStroke(Paint.valueOf("#d000ff"));
        selected = true;

        showRoute(vehicle.routeDataProperty.getValue().currentRoute);
        //mapPane.showRoute(vehicle.routeDataProperty.getValue().currentRoute);
    }

    /**
     * unselects bus
     */
    public void unSelect(){
        circle.setStroke(Paint.valueOf("#000000"));
        selected = false;
        mapPane.clearRoute();
    }

    private void showRoute(List<Node> nodes){
        mapPane.showRoute(nodes);
    }
}
