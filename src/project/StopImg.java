package project;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import project.map.Line;
import project.map.Node;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StopImg extends Group {

    public Image busStopIcon = new Image(Controller.class.getResourceAsStream("busStop.png"));

    @FXML
    private ImageView image;

    @FXML
    private Label name;

    private Node node;
    private boolean clicked;

    private Controller controller;
    private List<EdgeRoute> routes = new ArrayList<EdgeRoute>();

    public StopImg(Node node, Controller cntrl){
        this.node = node;
        this.controller = cntrl;

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
        clicked = false;

        name.setText(node.stop.name);

        image.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent ->{
            name.setVisible(true);
        });

        image.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent ->{
            name.setVisible(false);
        });

        image.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->{
            if(clicked){
                clicked = false;
                controller.clearRoute(this.routes);
            }
            else{
                clicked = true;
                for(Line line : controller.map.lines){
                    if(line.stops.contains(this.node)){
                        Color color = new Color((line.number * 47) % 255, (line.number * 7) % 255, (line.number * 11) % 255);
                        this.routes = controller.showRoute(line.getCurrentRoute(), color);
                    }
                }
            }
        });
    }

    public void setPos() {
        setTranslateX(node.x - 10);
        setTranslateY(node.y - 10);
    }
}
