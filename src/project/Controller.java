package project;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {
    public Button MainButton;
    public Label TextLabel;

    public void onButtonClick(ActionEvent actionEvent) {
        TextLabel.setText("Congratulations!");
    }
}
