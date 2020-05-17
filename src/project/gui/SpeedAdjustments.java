/*
soubor: project.gui.SpeedAdjustments.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: soubor je třída, ovládajicí prvky vytvořené v souboru SpeedAdjustments.fxml
        stará se o zobrazení informací o ulici a úpravu její rychlosti
 */

package project.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import project.Loader;
import project.map.Street;

/**
 * Class for modifying street values
 */
public class SpeedAdjustments extends VBox {

    @FXML
    private Label streetLabel;
    @FXML
    private Label speedLabel;
    @FXML
    private Slider slider;

    private Street street;
    //private double speed;

    /**
     * contructor of speed adjustments panel
     */
    public SpeedAdjustments() {
        Loader.loadFXMLDef(getClass().getResource("SpeedAdjustments.fxml"), this);
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
