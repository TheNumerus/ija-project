/*
soubor: project.gui.BusDetails.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: soubor je třída, ovládajicí prvky vytvořené v souboru BusDetails.fxml
        dostává informace o stavu vozidla a aktualizuje zobrazující se informace
 */

package project.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import project.Loader;
import project.Pair;
import project.map.Node;
import project.map.RouteData;
import project.map.Street;
import project.map.Vehicle;

import java.io.IOException;
import java.time.Duration;

/**
 * Class for displaying bus details
 */
public class BusDetails extends VBox {

    //private ObjectProperty<RouteData> currentVehicleData;
    private final ChangeListener<RouteData> changeListener;

    @FXML
    private Label busName;
    @FXML
    private Label lineName;
    @FXML
    private Label Delay;
    @FXML
    private Label Origin;
    @FXML
    private Label NextStop;
    @FXML
    private Label Terminus;
    @FXML
    private Label SkippedStops;
    @FXML
    private Label Stops;


    /**
     * constructor of BusDetails panel
     */
    public BusDetails(){
        Loader.loadFXMLDef(getClass().getResource("BusDetails.fxml"), this);
        this.changeListener = (observable, oldValue, newValue) -> updateInfo(newValue);
    }


    public void updateInfo(RouteData currentRouteData){
        // line number
        this.lineName.setText("Line: " + currentRouteData.line.number);

        //delay
        //TODO: delays
        double total = 0;
        for(Pair<Node, Duration> stop : currentRouteData.defaultRoute){
            if(stop.getX().stop.name.equals(currentRouteData.nextStop.stop.name)) break;
            total += stop.getY().getSeconds();
        }
        double delay = currentRouteData.currentRouteTime.getSeconds() - total;
        String minutes = Integer.toString((int)(delay % 3600) / 60);
        String seconds = Integer.toString((int)delay % 60);

        if(minutes.length() == 1) minutes = "0" + minutes;
        if(seconds.length() == 1) seconds = "0" + seconds;
        this.Delay.setText("Delay: " + minutes + ":" + seconds);

        // origin
        this.Origin.setText("Origin: " + currentRouteData.defaultRoute.get(0).getX().stop.name);

        // next stop
        this.NextStop.setText("Next stop: " + currentRouteData.nextStop.stop.name);

        // terminus
        this.Terminus.setText("Terminus: " + currentRouteData.defaultRoute.get(currentRouteData.defaultRoute.size() - 1).getX().stop.name);

        // skipped stops
        String skipped = "Skipped stops:\n";
        for(Node stop : currentRouteData.skippedStops){
            skipped += stop.stop.name + "\n";
        }
        this.SkippedStops.setText(skipped);

        // all stops
        String stops = "Stops: \n";
        for(Pair<Node, Duration> node: currentRouteData.defaultRoute){
            if(currentRouteData.skippedStops.contains(node.getX())){
                stops += node.getX().stop.name + "(skipped)\n";
            }
            else{
                stops += node.getX().stop.name + "\n";
            }
        }
        this.Stops.setText(stops);


    }

    public void setVehicle(Vehicle vehicle){
        vehicle.routeDataProperty.addListener(changeListener);
    }

}
