package project.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.fxml.FXMLLoader;
import project.map.Node;
import java.io.IOException;

public class EdgeRoute extends Group {
    @FXML
    private Line line;

    public Node node1;
    public Node node2;


    public EdgeRoute(Node node1, Node node2, String hexColor){
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
        line.setStyle("-fx-stroke: " + hexColor + ";");

        this.node1 = node1;
        this.node2 = node2;
    }

}
