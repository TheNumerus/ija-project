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

    /**
     * label displaying bus name
     */
    @FXML
    public Label busName;

    /**
     * label displaying line name
     */
    @FXML
    public Label lineName;

    /**
     * Label displaying current delay
     */
    @FXML
    public Label Delay;

    /**
     * Label displaying origin stop
     */
    @FXML
    public Label Origin;

    /**
     * Label displaying next stop
     */
    @FXML
    public Label NextStop;

    /**
     * Label displaying the last stop
     */
    @FXML
    public Label Terminus;

    /**
     * Label displaying skipped stops
     */
    @FXML Label SkippedStops;


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
        /*for(Node stop : currentRouteData.skippedStops){
            skipped += stop.stop.name + "\n";
        }*/
        this.SkippedStops.setText(skipped);

    }

    public void setVehicle(Vehicle vehicle){
        vehicle.routeDataProperty.addListener(changeListener);
    }

}
