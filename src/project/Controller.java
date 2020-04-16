package project;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import project.map.*;

public class Controller {
    public Label TextLabel;
    public Pane MapPane;
    public Group MapTransform;

    public Map map = new Map();

    private Node DragStart = new Node(0.0, 0.0);
    private Node OrigTransform = new Node(0.0, 0.0);

    public void initialize() {
        // TODO replace by reset only, map will be loaded from elsewhere
        loadMap(Map.placeholderData());
    }

    private void loadMap(Map map) {
        resetView(null);
        for (Street s: map.streets) {
            // TODO check for duplicate edges already on map
            for (Edge e: s.getEdges()) {
                EdgeLine el = new EdgeLine(e);
                MapTransform.getChildren().add(el);
            }
        }
    }

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

    public void resetView(ActionEvent actionEvent) {
        MapTransform.setTranslateX(640);
        MapTransform.setTranslateY(360);
        MapTransform.setScaleX(1);
        MapTransform.setScaleY(1);

    }
}