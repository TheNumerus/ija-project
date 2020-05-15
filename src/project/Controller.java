package project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.stage.FileChooser;

import project.gui.MapPane;
import project.map.*;

import java.io.File;
import java.time.Duration;

public class Controller {
    @FXML
    public Spinner<Integer> JumpSpinner;
    @FXML
    private Label time;
    @FXML
    private MapPane MapPane;
    public Button speedUp_button;
    public Button slowDown_button;
    public Button playPause_button;
    public Label speed;

    public Map map;

    private InternalClock clock;

    public void initialize() {
        clock = new InternalClock(this::tick);
        clock.setPaused(true);
    }

    public void close() {
        clock.cancel();
    }

    public void tick(Duration time, Duration delta) {
        setTime(time);
        map.onTick(delta);
        MapPane.renderVehicles(map);
    }

    private void loadMap() {
        MapPane.clearMap();
        MapPane.renderMapBase(map);
        MapPane.resetView();
        clock.setPaused(false);
        playPause_button.setDisable(false);
    }

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

    public void speedUp(ActionEvent actionEvent){
        double currentSpeed = clock.speedUp();
        slowDown_button.setDisable(false);
        if(currentSpeed == 10.0){
            speedUp_button.setDisable(true);
        }
        speed.setText(currentSpeed + "x");
    }

    public void slowDown(ActionEvent actionEvent){
        double currentSpeed = clock.speedDown();
        speedUp_button.setDisable(false);
        if(currentSpeed == 0.1){
            slowDown_button.setDisable(true);
        }
        speed.setText(currentSpeed + "x");
    }

    public void resetSpeed(ActionEvent actionEvent){
        clock.resetSpeed();
        speedUp_button.setDisable(false);
        slowDown_button.setDisable(false);
        speed.setText("1.0x");
    }

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

    public void resetView(ActionEvent actionEvent) {
        MapPane.resetView();
    }

    public void onJumpButtonPressed(ActionEvent actionEvent) {
        if (map != null) {
            clock.jumpForward(Duration.ofSeconds(JumpSpinner.getValue()));
        }
    }
}