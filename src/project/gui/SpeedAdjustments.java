package project.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import project.map.Street;

import java.io.IOException;

/**
 * Class for modifying street values
 */
public class SpeedAdjustments extends VBox {
    @FXML
    /**
     * label displaying name of the street
     */
    public Label streetLabel;
    @FXML
    /**
     * label displaying speed of the street
     */
    public Label speedLabel;
    @FXML
    private Slider slider;

    private Street street;
    //private double speed;

    /**
     * contructor of speed adjustments panel
     */
    public SpeedAdjustments() {
        // load ui elements
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "SpeedAdjustments.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(theClass -> this);

        try {
            fxmlLoader.load();
        } catch (
                IOException exception) {
            throw new RuntimeException(exception);
        }
        setDisable(true);

        slider.valueProperty().addListener((observable, newVal, oldVal) -> onValueChange(newVal.doubleValue()));
    }

    /**
     * sets the label displaying street name
     * @param street name of the street
     */
    public void setStreet(Street street) {
        this.street = street;
        setDisable(false);
        streetLabel.setText(street.name);
        slider.valueProperty().setValue(street.costMultiplier);
    }

    /**
     * unsets street info
     */
    public void unsetStreet() {
        street = null;
        setDisable(true);
    }

    /**
     * updates values, when sliders is moved
     * @param newVal new value of the slider
     */
    public void onValueChange(double newVal) {
        street.costMultiplier = newVal;
        newVal *= 100;
        newVal = Math.round(newVal);
        newVal /= 100;
        speedLabel.setText("Target speed: " + newVal + "x");

    }
}
