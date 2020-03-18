package project;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import project.map.Node;

public class Controller {
    public Button MainButton;
    public Label TextLabel;
    public Pane MapPane;
    public Group MapTransform;

    private Node<Double> DragStart = new Node<>(0.0, 0.0);
    private Node<Double> OrigTransform = new Node<>(0.0, 0.0);

    public void onButtonClick(ActionEvent actionEvent) {
        TextLabel.setText("Congratulations!");
    }

    public void onMousePressed(MouseEvent event) {
        DragStart.x = event.getSceneX();
        DragStart.y = event.getSceneY();
        OrigTransform.x = MapTransform.getTranslateX();
        OrigTransform.y = MapTransform.getTranslateY();
    }

    public void onMouseDragged(MouseEvent event) {
        double delta_x = event.getSceneX() - DragStart.x;
        double delta_y = event.getSceneY() - DragStart.y;
        MapTransform.setTranslateX(delta_x + OrigTransform.x);
        MapTransform.setTranslateY(delta_y + OrigTransform.y);
        event.consume();
    }

    public void onScroll(ScrollEvent event) {
        double val = event.getDeltaY() / 400.0;
        double new_zoom = Double.max(MapTransform.getScaleX() + val, 0.1);
        MapTransform.setScaleX(new_zoom);
        MapTransform.setScaleY(new_zoom);
    }
}
