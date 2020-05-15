package project.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
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
    private final Consumer<Boolean> onClose;

    @FXML
    private Line line;

    @FXML
    private Label name;

    public EdgeLine(Edge e, Street s, Consumer<Boolean> onClose) {
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

        closed = false;
        this.onClose = onClose;

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

    private void mouseEntered(MouseEvent mouseEvent) {
        line.getStyleClass().clear();
        if (closed) {
            line.getStyleClass().add("edgeline_closedhover");
        } else {
            line.getStyleClass().add("edgeline_hover");
        }
    }

    private void mouseClicked(MouseEvent mouseEvent) {
        line.getStyleClass().clear();
        if (closed) {
            line.getStyleClass().add("edgeline_hover");
        } else {
            line.getStyleClass().add("edgeline_closedhover");
        }
        closed = !closed;
        onClose.accept(closed);
    }

    private void mouseExited(MouseEvent mouseEvent) {
        line.getStyleClass().clear();
        if (closed) {
            line.getStyleClass().add("edgeline_closed");
        }
    }
}
