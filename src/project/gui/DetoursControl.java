/*
soubor: project.gui.DetoursControl.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: soubor je třída, ovládajicí prvky vytvořené v souboru DetoursControl.fxml
        obsahuje tlačítka, používané k ovládaní simulace
 */

package project.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import project.Loader;
import project.Pair;
import project.map.Edge;
import project.map.Map;
import project.map.Node;

import java.util.ArrayList;
import java.util.List;

public class DetoursControl extends VBox {
    public enum DetoursSelectState {
        SELECTING_CLOSURES,
        CLOSURE_SELECTED,
        SELECTING_DETOUR_ROUTE,
        NONE
    }

    @FXML
    private Button SelectClosedSegments;
    @FXML
    private Button SelectDetourSegments;
    @FXML
    private Button RemoveDetour;
    @FXML
    private Button Apply;

    private Map map;
    private MapPane mapPane;

    private List<Edge> activeSegment;

    private final List<List<Edge>> detourSegments = new ArrayList<>();
    private final List<Edge> detour = new ArrayList<>();

    public DetoursSelectState selectState = DetoursSelectState.NONE;

    public DetoursControl(Map map){
        Loader.loadFXMLDef(getClass().getResource("DetoursControl.fxml"), this);
        this.map = map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setMapPane(MapPane mapPane) {
        this.mapPane = mapPane;
    }

    /**
     * Method called then user selects some edge
     * @param segment list of edges
     */
    public void segmentSelected(List<Edge> segment) {
        switch (selectState) {
        case NONE:
            detourSegments.clear();
            detour.clear();
            break;
        case CLOSURE_SELECTED:
            break;
        case SELECTING_DETOUR_ROUTE:
            // TODO check for validity
            if (!detourSegments.contains(segment)) {
                if (map.isSegmentClosed(segment) || activeSegment.equals(segment)) {
                    return;
                }
                mapPane.highlightSegment(segment, false, false);
                detourSegments.add(segment);
                detour.addAll(segment);
            } else {
                mapPane.unHighlightSegment(segment);
                detourSegments.remove(segment);
                detour.removeAll(segment);
            }
            break;
        case SELECTING_CLOSURES:
            activeSegment = segment;
            if (map.isSegmentClosed(segment)) {
                mapPane.highlightSegment(segment, true, true);
                RemoveDetour.setDisable(false);
                SelectDetourSegments.setDisable(true);
                // TODO highlight detour part
            } else {
                mapPane.highlightSegment(segment, false, true);
                RemoveDetour.setDisable(true);
                SelectDetourSegments.setDisable(false);
            }
        }
    }

    @FXML
    private void SelectClosedSegmentsButtonClick(ActionEvent actionEvent){
        selectState = DetoursSelectState.SELECTING_CLOSURES;
    }

    @FXML
    private void SelectDetourSegmentsButtonClick(ActionEvent actionEvent){
        mapPane.highlightSegment(activeSegment, true, true);
        selectState = DetoursSelectState.SELECTING_DETOUR_ROUTE;
        SelectClosedSegments.setDisable(true);
        Apply.setDisable(false);
    }

    @FXML
    private void ApplyButtonClick(ActionEvent actionEvent){
        if (!map.addDetour(new Pair<>(activeSegment, detour))) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Invalid detour path");
            a.showAndWait();
            return;
        }

        selectState = DetoursSelectState.NONE;
        Apply.setDisable(true);

        SelectDetourSegments.setDisable(true);
        SelectClosedSegments.setDisable(false);
        mapPane.highlightStreet(null);
        mapPane.updateEdgeLineState();

        detourSegments.clear();
        detour.clear();
    }

    @FXML
    private void RemoveButtonClick(ActionEvent actionEvent) {
        selectState = DetoursSelectState.NONE;
        map.removeDetour(activeSegment);
        mapPane.highlightStreet(null);
        mapPane.updateEdgeLineState();
    }

    /**
     * Resets state of this class
     */
    public void resetState() {
        selectState = DetoursSelectState.NONE;
        detour.clear();
        detourSegments.clear();
        activeSegment = null;
        SelectClosedSegments.setDisable(false);
        SelectDetourSegments.setDisable(true);
        Apply.setDisable(true);
        RemoveDetour.setDisable(true);
    }
}
