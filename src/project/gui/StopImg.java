/*
soubor: project.gui.StopImg.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: soubor je třída, ovládajicí prvky vytvořené v souboru StopImg.fxml
        vytváří a nastavuje pozici obrázku, reprezentující zastávku
 */

package project.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import project.Loader;
import project.map.Line;
import project.map.Map;
import project.map.Node;

import java.awt.*;
import java.io.IOException;

/**
 * Class for interacting with stops
 */
public class StopImg extends Group {

    @FXML
    private ImageView image;

    @FXML
    private Label name;

    private final Node node;

    /**
     * constructor of stop image
     * @param node node, the stop is situated on
     * @param map map data
     * @param pane pane data
     */
    public StopImg(Node node, Map map, MapPane pane){
        Loader.loadFXMLDef(getClass().getResource("StopImg.fxml"), this);
        this.node = node;

        name.setVisible(false);

        name.setText(node.stop.name);

        image.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent ->{
            name.setVisible(true);
        });

        image.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent ->{
            name.setVisible(false);
        });

        image.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->{
            for(Line line : map.lines) {
                if(line.getNodes().contains(this.node)){
                    Color color = new Color((line.number * 47) % 255, (line.number * 7) % 255, (line.number * 11) % 255);
                    pane.showRoute(line.getDefaultRoute(), color);
                }
            }
        });
    }

    /**
     * sets positions of image to node positions
     */
    public void setPos() {
        setTranslateX(node.x - 10);
        setTranslateY(node.y - 10);
    }
}
