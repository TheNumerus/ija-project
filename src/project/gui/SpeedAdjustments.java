package project.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import project.Controller;
import project.map.Edge;
import javafx.fxml.FXMLLoader;
import project.map.Street;

import java.io.IOException;
import java.util.function.Consumer;


import java.io.IOException;

public class SpeedAdjustments extends GridPane {

    @FXML
    public ScrollBar scrollbar;

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
    }

}
