package project.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import project.map.Street;

import java.io.IOException;

/**
 * Class for displaying bus details
 */
public class BusDetails extends VBox {

    @FXML
    /**
     * label displaying bus name
     */
    public Label busName;
    @FXML
    /**
     * label displaying line name
     */
    public Label lineName;


    /**
     * constructor of BusDetails panel
     */
    public BusDetails(){
        // load ui elements
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "BusDetails.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(theClass -> this);

        try {
            fxmlLoader.load();
        } catch (
                IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * updates line number label
     * @param lineNumber number of ne line
     */
    public void updateInfo(int lineNumber){
        this.lineName.setText(Integer.toString(lineNumber));
    }

}
