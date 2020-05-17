package project.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import project.Loader;
import project.map.Street;

import java.io.IOException;

/**
 * Class for displaying bus details
 */
public class BusDetails extends VBox {


    /**
     * label displaying bus name
     */
    @FXML
    public Label busName;

    /**
     * label displaying line name
     */
    @FXML
    public Label lineName;


    /**
     * constructor of BusDetails panel
     */
    public BusDetails(){
        Loader.loadFXMLDef(getClass().getResource("BusDetails.fxml"), this);
    }

    /**
     * updates line number label
     * @param lineNumber number of ne line
     */
    public void updateInfo(int lineNumber){
        this.lineName.setText(Integer.toString(lineNumber));
    }

}
