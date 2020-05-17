package project.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import project.gui.MapPane;
import project.map.Line;
import project.map.Map;
import project.map.Node;

import java.awt.*;
import java.io.IOException;

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
        this.node = node;
        // load ui elements
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "StopImg.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(theClass -> this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

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
