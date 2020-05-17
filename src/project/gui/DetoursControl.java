package project.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import project.Loader;

public class DetoursControl extends VBox {

    @FXML
    private Button SelectClosedSegments;
    @FXML
    private Button SelectDetourSegments;
    @FXML
    private Button Apply;

    public DetoursControl(){
        Loader.loadFXMLDef(getClass().getResource("DetoursControl.fxml"), this);
    }

    @FXML
    public void SelectClosedSegmentsButtonClick(ActionEvent actionEvent){
        System.out.println("closed segments");
    }

    @FXML
    public void SelectDetourSegmentsButtonClick(ActionEvent actionEvent){
        System.out.println("detour segments");
    }

    @FXML
    public void ApplyButtonClick(ActionEvent actionEvent){
        System.out.println("apply");
    }
}
