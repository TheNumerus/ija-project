/*
soubor: project.gui.MapPane.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: soubor je třída, ovládajicí prvky vytvořené v souboru MapPane.fxml
        obsahuje metody na ovládaní zobrazení mapy, zvýrazňování ulic a tras, vykreslování dopravních prostředků
 */

package project.gui;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import project.Controller;
import project.EditMode;
import project.Loader;
import project.Pair;
import project.map.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for map rendering
 */
public class MapPane extends Pane {
    @FXML
    private Group MapTransform;

    private Controller controller;

    private final Pair<Double, Double> DragStart = new Pair<>(0.0, 0.0);
    private final Pair<Double, Double> OrigTransform = new Pair<>(0.0, 0.0);

    private final List<VehicleUI> vehicles = new ArrayList<>();
    private final List<EdgeRoute> routes = new ArrayList<>();

    private boolean dragging = false;

    /**
     * constructor
     */
    public MapPane() {
        Loader.loadFXMLDef(getClass().getResource("MapPane.fxml"), this);
    }


    /**
     * Sets parent window controller
     * @param controller controller to set
     */
    public void setController(Controller controller){
        this.controller = controller;
    }

    /**
     * Adds object to be rendered on map
     * @param node Object to be added to map
     */
    public void addNode(Node node) {
        MapTransform.getChildren().add(node);
    }

    /**
     * Removes object from map
     * @param node object to be removed
     */
    public void removeNode(Node node) {
        MapTransform.getChildren().remove(node);
    }

    /**
     * Clears entire map
     */
    public void clearMap() {
        MapTransform.getChildren().clear();
    }

    //region event handlers

    /**
     * stores coordinates of where mouse was pressed for moving with the map
     * @param event action event
     */
    public void onMousePressed(MouseEvent event) {
        DragStart.setX(event.getSceneX());
        DragStart.setY(event.getSceneY());
        OrigTransform.setX(MapTransform.getTranslateX());
        OrigTransform.setY(MapTransform.getTranslateY());
        dragging = false;
        event.consume();
    }

    /**
     * moves with mapPane accordingly to dragging
     * @param event action event
     */
    public void onMouseDragged(MouseEvent event) {
        dragging = true;
        double delta_x = event.getSceneX() - DragStart.getX();
        double delta_y = event.getSceneY() - DragStart.getY();
        MapTransform.setTranslateX(delta_x + OrigTransform.getX());
        MapTransform.setTranslateY(delta_y + OrigTransform.getY());
        event.consume();
    }

    /**
     * zooms in/out
     * @param event action event
     */
    public void onScroll(ScrollEvent event) {
        double val = event.getDeltaY() / 400.0;
        double new_zoom = Double.max(MapTransform.getScaleX() + val, 0.1);
        MapTransform.setScaleX(new_zoom);
        MapTransform.setScaleY(new_zoom);
    }

    /**
     * cancels all selections
     * @param event action event
     */
    public void onMouseClicked(MouseEvent event){
        if(event.getTarget().equals(this) && !dragging && controller.currentMode != EditMode.DETOURS) {
            clearRoute();
            highlightStreet(null);
            controller.busUnClicked();
        }
    }

    //endregion

    //region line highlights

    /**
     * deletes all routes highlights
     */
    public void clearRoute(){
        for (EdgeRoute routeEdge : routes) {
            removeNode(routeEdge);
        }
        routes.clear();
    }


    /**
     * deletes specific routes highlights
     * @param routes routes to delete from map
     */
    public void clearRoute(List<EdgeRoute> routes){
        this.routes.removeAll(routes);
        for(EdgeRoute line: routes){
            removeNode(line);
        }
    }

    /**
     * generates default route highlight color and generates route
     * @param nodes list of points on the map, which to highlight route between
     */
    public void showRoute(List<project.map.Node> nodes){
        Color defaultColor = new Color(255, 213, 3);
        showRoute(nodes, defaultColor);
    }


    /**
     * generates new highlights by given nodes and adds them to map
     * @param nodes list of points on the map, which to highlight route between
     * @param color color to use to highlight
     */
    public void showRoute(List<project.map.Node> nodes, Color color){
        if (nodes == null) {
            return;
        }
        // generating hex string
        String hexColor = Integer.toHexString(color.getRGB() & 0xffffff);
        if(hexColor.length() < 6){
            hexColor = "0" + hexColor;
        }
        hexColor = "#" + hexColor;

        if(nodes.isEmpty()){
            return;
        }

        //adding route
        for (int i = 1; i < nodes.size(); i++){
            EdgeRoute e = new EdgeRoute(nodes.get(i - 1), nodes.get(i), hexColor);
            e.mouseTransparentProperty().setValue(true);
            addNode(e);
            routes.add(e);
        }

        //adding stops back to front
        List<Node> stops = MapTransform.getChildren().stream().filter(n -> n instanceof StopImg).collect(Collectors.toList());
        stops.forEach(Node::toFront);
    }

    /**
     * highlights street
     * @param onStreet street to highlight
     */
    public void highlightStreet(Street onStreet) {
        // get all edgelines
        List<EdgeLine> streetEdges = MapTransform.getChildren().stream().filter(c ->
            c instanceof EdgeLine
        ).map(c -> ((EdgeLine)c)).collect(Collectors.toList());
        // disable highlight
        streetEdges.forEach(n -> n.setHighlight(false, false));
        streetEdges.stream().filter( e -> controller.map.isEdgeClosed(e.getEdge().start, e.getEdge().end)).forEach(e -> e.setHighlight(true, true));

        //now add highlight only to some edges
        streetEdges.stream().filter(c -> c.getOnStreet().equals(onStreet)).forEach(c -> c.setHighlight(true, false));
    }

    public void highlightSegment(List<Edge> edges, boolean highlightAsClosure, boolean clear) {
        // get all edgelines
        List<EdgeLine> streetEdges = MapTransform.getChildren().stream().filter(c ->
                c instanceof EdgeLine
        ).map(c -> ((EdgeLine)c)).collect(Collectors.toList());
        if (clear) {
            // disable highlight
            streetEdges.forEach(n -> n.setHighlight(false, false));
        }

        streetEdges.stream().filter(e -> edges.contains(e.getEdge())).forEach(e -> e.setHighlight(true, highlightAsClosure));
    }

    public void unHighlightSegment(List<Edge> segment) {
        // get all edgelines
        List<EdgeLine> streetEdges = MapTransform.getChildren().stream().filter(c ->
                c instanceof EdgeLine
        ).map(c -> ((EdgeLine)c)).collect(Collectors.toList());

        streetEdges.stream().filter(e -> segment.contains(e.getEdge())).forEach(e -> e.setHighlight(false, false));
    }

    public void updateEdgeLineState() {
        List<EdgeLine> streetEdges = MapTransform.getChildren().stream().filter(c ->
                c instanceof EdgeLine
        ).map(c -> ((EdgeLine)c)).collect(Collectors.toList());

        streetEdges.forEach(s -> s.setClosed(controller.map.isEdgeClosed(s.getEdge().start, s.getEdge().end)));
    }

    //endregion
    //region public methods

    /**
     * Renderers map base such as streets, stops
     * @param map map data
     */
    public void renderMapBase(Map map) {
        for (Street s: map.streets) {
            for (Edge e: s.getEdges()) {
                EdgeLine el = new EdgeLine(e, s, (closed) -> {
                    if (closed) {
                        map.closures.add(new Pair<>(e.start, e.end));
                    } else {
                        for (Pair<List<project.map.Node>, ?> detour: map.detours) {
                            if (detour.getX().contains(e.start) && detour.getX().contains(e.end)) {
                                return true;
                            }
                        }
                        map.detours.removeIf(detour -> detour.getX().contains(e.start) && detour.getX().contains(e.end));
                        map.closures.remove(new Pair<>(e.start, e.end));
                    }
                    return closed;
                }, controller, this);
                addNode(el);

                if(e.start.stop != null){
                    StopImg busStop = new StopImg(e.start, map, this);
                    addNode(busStop);
                    busStop.setPos();
                }
            }
        }
    }

    /**
     * Creates new vehicles and updates position on existing vehicles
     * @param m map with vehicle data
     */
    public void renderVehicles(Map m) {
        for (Vehicle v: m.vehicles) {
            List<VehicleUI> filtered = vehicles.stream().filter((vui) -> vui.getVehicle().equals(v)).collect(Collectors.toList());
            if (filtered.size() > 0) {
                filtered.get(0).newPos();
            } else {
                VehicleUI vui = new VehicleUI(v, controller, this);
                addNode(vui);
                vehicles.add(vui);
                vui.newPos();
            }
        }
    }

    /**
     * Resets map view
     *
     * Map will be centered and scaled to view afterwards
     */
    public void resetView() {
        MapTransform.setTranslateX(0);
        MapTransform.setTranslateY(0);
        // get pane size
        double width = getWidth();
        double height = getHeight();
        // reset child scale
        MapTransform.setScaleX(1);
        MapTransform.setScaleY(1);
        // get content size
        Bounds b = MapTransform.getBoundsInParent();
        // scale to fit content
        double minScale = Math.min(width / b.getWidth(), height / b.getHeight()) * 0.95;
        MapTransform.setScaleX(minScale);
        MapTransform.setScaleY(minScale);

        // get new content size
        b = MapTransform.getBoundsInParent();

        double center_x = b.getMinX();
        double center_y = b.getMinY();

        // now center
        MapTransform.setTranslateX(-center_x + (width - b.getWidth()) / 2);
        MapTransform.setTranslateY(-center_y + (height - b.getHeight()) / 2);
    }

    /**
     * removing highlights, if mode is changed
     * @param newEditMode mode that was switched to
     */
    public void editModeChanged(EditMode newEditMode) {
        // remove all highlights on mode change
        highlightStreet(null);
    }
}
