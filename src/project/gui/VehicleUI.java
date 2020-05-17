package project.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import project.Controller;
import project.Loader;
import project.map.Vehicle;
import java.io.IOException;

/**
 * Class representing vehicle on map
 */
public class VehicleUI extends Circle {
    private final Controller controller;
    private final Vehicle vehicle;

    @FXML
    Circle circle;

    /**
     * constructor of vehicle
     * @param v vehicle, that is represented
     * @param c controller data
     */
    public VehicleUI(Vehicle v, Controller c) {
        Loader.loadFXMLDef(getClass().getResource("VehicleUI.fxml"), this);

        setRadius(10.0);
        vehicle = v;
        controller = c;
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
        controller.busClicked(vehicle);
    }
}
