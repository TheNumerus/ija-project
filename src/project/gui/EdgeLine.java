/*
soubor: project.gui.EdgeLine.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: soubor je třída, ovládajicí prvky vytvořené v souboru EdgeLine.fxml
        nastavuje umístění čáry, zobrazování popisku, zváraznění a zpracovává vyvolané eventy
 */

package project.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import project.Controller;
import project.EditMode;
import project.Loader;
import project.map.Edge;
import javafx.fxml.FXMLLoader;
import project.map.Street;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * Class for interacting with map
 *
 * Renders clickable line, which is line between two nodes in street.
 * Lines are closed this way
 */
public class EdgeLine extends Group {
    private boolean closed;
    private boolean selected;
    private final Predicate<Boolean> onClose;
    private final Street onStreet;
    private final Controller controller;
    private final MapPane mapPane;

    private final Edge e;

    @FXML
    private Line line;

    @FXML
    private Label name;

    /**
     * constructor
     * @param e which edge does this represent
     * @param s which street is this edgeline on
     * @param onClose function called on street close
     * @param controller controller data
     * @param mapPane MapPane data to put this on
     */
    public EdgeLine(Edge e, Street s, Predicate<Boolean> onClose, Controller controller, MapPane mapPane) {
        Loader.loadFXMLDef(getClass().getResource("EdgeLine.fxml"), this);

        this.controller = controller;
        closed = false;
        selected = false;
        this.onClose = onClose;
        this.onStreet = s;
        this.mapPane = mapPane;
        this.e = e;

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
     * @param asClosure should the edge be red
     */
    public void setHighlight(boolean highlight, boolean asClosure) {
        line.getStyleClass().clear();
        if (highlight) {
            if (asClosure) {
                line.getStyleClass().add("edgeline_closed");
            } else {
                line.getStyleClass().add("edgeline_hover");
            }
        } else if (closed) {
            line.getStyleClass().add("edgeline_closed");
        }
    }

    /**
     * Sets the closed status and updated highlight
     * @param closed state
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
        line.getStyleClass().clear();
        if (closed) {
            line.getStyleClass().add("edgeline_closed");
        }
    }

    /**
     * Returns edge associated with this object
     * @return edge
     */
    public Edge getEdge() {
        return e;
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
            closed = !closed;
            closed = onClose.test(closed);
            if (!closed) {
                line.getStyleClass().add("edgeline_hover");
            } else {
                line.getStyleClass().add("edgeline_closedhover");
            }
        }
        else if(controller.currentMode == EditMode.SPEEDADJUSTMENTS){
            mapPane.highlightStreet(onStreet);
            controller.setStreetSelect(onStreet);
        } else if (controller.currentMode == EditMode.DETOURS) {
            controller.setDetourSegmentSelect(e);
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
