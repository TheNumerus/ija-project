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
    public Label streetLabel;
    @FXML
    private Slider slider;

    private Street street;

    public SpeedAdjustments(){
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

    public void setStreet(Street street) {
        this.street = street;
        setDisable(false);
        streetLabel.setText(street.name);
        slider.valueProperty().setValue(street.costMultiplier);
    }

    public void unsetStreet() {
        street = null;
        setDisable(true);
    }

    public void onValueChange(double newVal) {
        street.costMultiplier = newVal;
    }
}
