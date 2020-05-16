package project.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import project.Controller;
import project.map.Vehicle;
import java.io.IOException;

public class VehicleUI extends Circle {

    private final Controller controller;
    private final Vehicle vehicle;

    @FXML
    Circle circle;

    public VehicleUI(Vehicle v, Controller c) {
        // load ui elements
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "VehicleUI.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(theClass -> this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setRadius(10.0);
        vehicle = v;
        controller = c;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void newPos() {
        setTranslateX(vehicle.getX());
        setTranslateY(vehicle.getY());
    }

    @FXML
    private void mouseClicked(MouseEvent mouseEvent){
        controller.busClicked(vehicle);
    }
}
