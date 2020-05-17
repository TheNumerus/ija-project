/*
soubor: project.gui.EdgeRoute.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: soubor je třída, ovládajicí prvky vytvořené v souboru EdgeRoute.fxml
        nastavuje barvu a pozici čáry, reprezentující trasu
 */

package project.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.fxml.FXMLLoader;
import project.Loader;
import project.map.Node;
import java.io.IOException;

/**
 * Class for interacting with map
 *
 * Renders line between two nodes.
 */

public class EdgeRoute extends Group {
    @FXML
    private Line line;

    /**
     * starting node
     */
    public Node node1;
    /**
     * ending node
     */
    public Node node2;


    /**
     * contructor
     * @param node1 starting node
     * @param node2 ending node
     * @param hexColor color of the line in hex format
     */
    public EdgeRoute(Node node1, Node node2, String hexColor){
        Loader.loadFXMLDef(getClass().getResource("EdgeRoute.fxml"), this);

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
