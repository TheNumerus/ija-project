package project;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import project.map.Edge;
import javafx.fxml.FXMLLoader;
import project.map.Node;
import project.map.Street;

import java.io.IOException;

public class EdgeRoute extends Group {
    @FXML
    private Line line;


    public EdgeRoute(Node node1, Node node2){
        // load ui elements
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "EdgeRoute.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(theClass -> this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        // line coordinates
        line.setStartX(node1.x);
        line.setStartY(node1.y);
        line.setEndX(node2.x);
        line.setEndY(node2.y);
    }

}
