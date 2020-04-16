package project;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import project.map.Edge;

public class EdgeLine extends Line {
    private Edge edge;
    private boolean hover;

    public EdgeLine(Edge e) {
        edge = e;
        hover = false;

        // line coordinates
        setStartX(e.start.x);
        setStartY(e.start.y);
        setEndX(e.end.x);
        setEndY(e.end.y);

        // mouse events
        addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            hover = true;
            getStyleClass().clear();
            if (edge.closed) {
                getStyleClass().add("edgeline_closedhover");
            } else {
                getStyleClass().add("edgeline_hover");
            }
        });
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            getStyleClass().clear();
            if (edge.closed) {
                getStyleClass().add("edgeline_hover");
            } else {
                getStyleClass().add("edgeline_closedhover");
            }
            edge.closed = !edge.closed;
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            getStyleClass().clear();
            hover = false;
            if (edge.closed) {
                getStyleClass().add("edgeline_closed");
            }
        });
    }
}
