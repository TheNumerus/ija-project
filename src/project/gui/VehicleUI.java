package project.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.shape.Circle;
import project.Controller;
import project.map.Vehicle;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javafx.scene.Group;

public class VehicleUI extends Group {

    private Controller controller;
    private final Vehicle vehicle;

    @FXML
    Circle circle;

    public VehicleUI(Vehicle v, Controller controller) {
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

        //super(10.0);
        circle.setRadius(10.0);
        vehicle = v;
        this.controller = controller;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void newPos() {
        circle.setTranslateX(vehicle.getX());
        circle.setTranslateY(vehicle.getY());
        //setTranslateX(vehicle.getX());
        //setTranslateY(vehicle.getY());
    }

    @FXML
    private void mouseClicked(MouseEvent mouseEvent){
        System.out.println("here");
        int number = this.vehicle.getLineNumber();
        controller.busDetails.updateInfo(number);
        controller.Sidebar.setCenter(controller.busDetails);
    }
}
