package project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import project.gui.BusDetails;
import project.gui.MapPane;
import project.gui.SpeedAdjustments;
import project.map.*;

import java.io.File;
import java.time.Duration;

public class Controller {
    @FXML
    public Spinner<Integer> JumpSpinner;
    @FXML
    public BorderPane Sidebar;
    @FXML
    private Label time;
    @FXML
    private Pane UICenter;
    @FXML
    private MapPane MapPane;
    public Button speedUp_button;
    public Button slowDown_button;
    public Button playPause_button;
    public Button closures_button;
    public Button speedAdjustments_button;
    public Button detours_button;
    public Label speed;

    private Street selectedStreet;


    public Map map;
    public SpeedAdjustments speedAdjustments;
    public BusDetails busDetails;

    private InternalClock clock;

    public EditMode currentMode = EditMode.CLOSURES;

    public void initialize() {
        clock = new InternalClock(this::tick);
        clock.setPaused(true);
        MapPane.setController(this);
        this.speedAdjustments = new SpeedAdjustments();
        this.busDetails = new BusDetails();
    }

    public void close() {
        clock.cancel();
    }

    public void tick(Duration time, Duration delta) {
        setTime(time);
        map.onTick(delta);
        MapPane.renderVehicles(map);
    }

    // sets the environment for new map
    public void loadMap() {
        MapPane.clearMap();
        MapPane.renderMapBase(map);
        MapPane.resetView();
        clock.resetSpeed();
        clock.resetTime();
        clock.setPaused(false);
        playPause_button.setDisable(false);
    }

    /**
     * After Load Data button, file chooser is opened.
     * Loads .json file and then loads map.
     * Shows popup alert, if the map file is not valid.
     *
     * @param actionEvent button event
     */
    public void loadDataButtonClick(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open resource file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Map file", "*.json")
        );

        File file = fileChooser.showOpenDialog(((Button)actionEvent.getTarget()).getScene().getWindow());
        if (file != null) {
            map = Loader.LoadMap(file);
            if (map == null) {
                // show popup or something
                Alert a = new Alert(Alert.AlertType.ERROR, "File contents not valid map.");
                a.showAndWait();
                return;
            }

            loadMap();
        }
    }


    /**
     * calculates hours, minutes and seconds (hh:mm:ss) from Duration.
     * @param time sets Label to this time
     */
    public void setTime(Duration time){
        long totalSecs = time.getSeconds();
        String hours = Integer.toString((int)totalSecs / 3600);
        String minutes = Integer.toString((int)(totalSecs % 3600) / 60);
        String seconds = Integer.toString((int)totalSecs % 60);

        if(hours.length() == 1) hours = "0" + hours;
        if(minutes.length() == 1) minutes = "0" + minutes;
        if(seconds.length() == 1) seconds = "0" + seconds;

        this.time.setText(hours + ":" + minutes + ":" + seconds);
    }

    public void resetTime(){
        this.time.setText("00:00:00");
    }

    /**
     * Disables clicked button and enables others.
     *
     * Sets current mode to clicked button mode.
     * @param actionEvent event
     */
    public void closuresButtonClick(ActionEvent actionEvent){
        this.closures_button.setDisable(true);
        this.speedAdjustments_button.setDisable(false);
        this.detours_button.setDisable(false);

        this.currentMode = EditMode.CLOSURES;

        Sidebar.setCenter(null);
        MapPane.editModeChanged(currentMode);
    }

    /**
     * Disables clicked button and enables others.
     *
     * Sets current mode to clicked button mode.
     * @param actionEvent event
     */
    public void speedAdjustmentsButtonClick(ActionEvent actionEvent){
        this.closures_button.setDisable(false);
        this.speedAdjustments_button.setDisable(true);
        this.detours_button.setDisable(false);

        this.currentMode = EditMode.SPEEDADJUSTMENTS;

        Sidebar.setCenter(speedAdjustments);
        MapPane.editModeChanged(currentMode);
    }

    /**
     * Disables clicked button and enables others.
     *
     * Sets current mode to clicked button mode.
     * @param actionEvent event
     */
    public void detoursButtonClick(ActionEvent actionEvent){
        this.closures_button.setDisable(false);
        this.speedAdjustments_button.setDisable(false);
        this.detours_button.setDisable(true);

        this.currentMode = EditMode.DETOURS;

        Sidebar.setCenter(null);
        MapPane.editModeChanged(currentMode);
    }


    // speeds up the simulation speed.
    // disables button, if the simulation speed is on maximum
    // enables slow down button.
    public void speedUp(ActionEvent actionEvent){
        double currentSpeed = clock.speedUp();
        slowDown_button.setDisable(false);
        if(currentSpeed == 10.0){
            speedUp_button.setDisable(true);
        }
        speed.setText(currentSpeed + "x");
    }

    // slows down the simulation speed.
    // disables button, if the simulation speed is on minimum.
    // enables speed up button
    public void slowDown(ActionEvent actionEvent){
        double currentSpeed = clock.speedDown();
        speedUp_button.setDisable(false);
        if(currentSpeed == 0.1){
            slowDown_button.setDisable(true);
        }
        speed.setText(currentSpeed + "x");
    }

    // resets the simulation speed to default.
    // enables speedUp and slowDown buttons.
    public void resetSpeed(ActionEvent actionEvent){
        clock.resetSpeed();
        speedUp_button.setDisable(false);
        slowDown_button.setDisable(false);
        speed.setText("1.0x");
    }

    // stops the simulation clock
    // changes between simulation states
    public void playPause(ActionEvent actionEvent){
        if(clock.isPaused()){
            clock.setPaused(false);
            playPause_button.setText("Pause");
        }
        else{
            clock.setPaused(true);
            playPause_button.setText("Play");
        }
    }


    // resets the view to default (X and Y axes and scroll)
    public void resetView(ActionEvent actionEvent) {
        MapPane.resetView();
    }

    public void onJumpButtonPressed(ActionEvent actionEvent) {
        if (map != null) {
            clock.jumpForward(Duration.ofSeconds(JumpSpinner.getValue()));
        }
    }

    public void setStreetSelect(Street onStreet) {
        selectedStreet = onStreet;
        if (currentMode == EditMode.SPEEDADJUSTMENTS) {
            speedAdjustments.setStreet(onStreet);
        }
    }

    public void busClicked(Vehicle v) {
        busDetails.updateInfo(v.getLineNumber());
        Sidebar.setCenter(busDetails);
    }
}