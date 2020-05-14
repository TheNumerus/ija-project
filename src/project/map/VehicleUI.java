package project.map;

import javafx.scene.shape.Circle;

public class VehicleUI extends Circle {
    private final Vehicle vehicle;
    public VehicleUI(Vehicle v) {
        super(10.0);
        vehicle = v;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void newPos() {
        setTranslateX(vehicle.getX());
        setTranslateY(vehicle.getY());
    }
}
