package project;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import project.map.Node;

import java.io.IOException;

public class StopImg extends Group {

    public Image busStopIcon = new Image(Controller.class.getResourceAsStream("busStop.png"));

    @FXML
    private ImageView image;

    @FXML
    private Label name;

    private Node node;

    public StopImg(Node node){
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
    }

    public void setPos() {
        setTranslateX(node.x - 10);
        setTranslateY(node.y - 10);
    }
}
