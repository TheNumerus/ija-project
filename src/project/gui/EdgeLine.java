package project.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import project.Controller;
import project.EditMode;
import project.map.Edge;
import javafx.fxml.FXMLLoader;
import project.map.Street;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Class for interacting with map
 *
 * Renders clickable line, which is line between two nodes in street.
 * Lines are closed this way
 */
public class EdgeLine extends Group {
    private boolean closed;
    public boolean selected;
    private final Consumer<Boolean> onClose;
    private final Street onStreet;
    private final Controller controller;
    private final MapPane mapPane;

    @FXML
    private Line line;

    @FXML
    private Label name;

    public EdgeLine(Edge e, Street s, Consumer<Boolean> onClose, Controller controller, MapPane mapPane) {
        // load ui elements
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "EdgeLine.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(theClass -> this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.controller = controller;
        closed = false;
        selected = false;
        this.onClose = onClose;
        this.onStreet = s;
        this.mapPane = mapPane;

        // line coordinates
        line.setStartX(e.start.x);
        line.setStartY(e.start.y);
        line.setEndX(e.end.x);
        line.setEndY(e.end.y);

        // rotate text
        double delta_x = (e.end.x - e.start.x);
        double delta_y = (e.end.y - e.start.y);
        double delta_ratio = delta_y / delta_x;
        double delta_length = Math.sqrt(delta_x * delta_x + delta_y * delta_y);

        name.translateXProperty().bind(name.widthProperty().divide(2).negate().add((e.start.x + e.end.x) / 2.0 - delta_y / delta_length * 10.0));
        name.translateYProperty().bind(name.heightProperty().divide(2).negate().add((e.start.y + e.end.y) / 2.0 + delta_x / delta_length * 10.0));

        double angle = Math.toDegrees(Math.atan(delta_ratio));
        name.setRotate(angle);

        // hide text on small segments
        if (delta_length < 100.0) {
            name.setVisible(false);
        }

        Tooltip tooltip = new Tooltip(s.name);
        Tooltip.install(line, tooltip);

        // street name
        name.setText(s.name);

        // mouse events
        line.addEventHandler(MouseEvent.MOUSE_ENTERED, this::mouseEntered);
        line.addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseClicked);
        line.addEventHandler(MouseEvent.MOUSE_EXITED, this::mouseExited);
    }

    /**
     * returns street associated with this edgeline
     * @return street
     */
    public Street getOnStreet() {
        return onStreet;
    }

    /**
     * Sets highlight effect or removes it
     * @param highlight do effect
     */
    public void setHighlight(boolean highlight) {
        line.getStyleClass().clear();
        if (highlight) {
            line.getStyleClass().add("edgeline_hover");
        } else if (closed) {
            line.getStyleClass().add("edgeline_closed");
        }
    }

    private void mouseEntered(MouseEvent mouseEvent) {
        if(controller.currentMode == EditMode.CLOSURES){
            line.getStyleClass().clear();
            if (closed) {
                line.getStyleClass().add("edgeline_closedhover");
            }
            else {
                line.getStyleClass().add("edgeline_hover");
            }
        }
    }

    private void mouseClicked(MouseEvent mouseEvent) {
        if(controller.currentMode == EditMode.CLOSURES){
            line.getStyleClass().clear();
            if (closed) {
                line.getStyleClass().add("edgeline_hover");
            } else {
                line.getStyleClass().add("edgeline_closedhover");
            }
            closed = !closed;
            onClose.accept(closed);
        }
        else if(controller.currentMode == EditMode.SPEEDADJUSTMENTS){
            mapPane.highlightStreet(onStreet);
            controller.setStreetSelect(onStreet);
        }
    }

    private void mouseExited(MouseEvent mouseEvent) {
        if (controller.currentMode == EditMode.CLOSURES) {
            line.getStyleClass().clear();
            if (closed) {
                line.getStyleClass().add("edgeline_closed");
            }
        }
    }
}
