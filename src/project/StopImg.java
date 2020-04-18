package project;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import project.map.Edge;

import java.io.IOException;

public class StopImg extends Group {

    public Image busStopIcon = new Image(Controller.class.getResourceAsStream("busStop.png"));

    @FXML
    private ImageView image;

    @FXML
    private Label name;

    public StopImg(Edge edge, boolean start){
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


        if(start){
            ImageView busStop = new ImageView(busStopIcon);
            name.setText(edge.start.stop.name);

            busStop.setFitHeight(20);
            busStop.setFitWidth(20);

            busStop.setX(edge.start.x - 10);
            busStop.setY(edge.start.y - 10);
        }
        else {
            ImageView busStop = new ImageView(busStopIcon);
            name.setText(edge.end.stop.name);

            busStop.setFitHeight(20);
            busStop.setFitWidth(20);

            busStop.setTranslateX(edge.end.x - 10);
            busStop.setTranslateY(edge.end.y - 10);
        }



        image.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent ->{
            name.setVisible(true);
        });

        image.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent ->{
            name.setVisible(false);
        });


    }
}
