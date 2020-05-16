package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        Controller c = loader.getController();
        Scene scene = new Scene(root, 1280, 720);
        this.controller = c;

        //default map loading
        loadDefaultMap("novigrad.json");

        // window settings
        primaryStage.setTitle("IJA Map");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(360);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.setOnHidden(e -> c.close());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void loadDefaultMap(String mapName){
        String path = System.getProperty("user.dir");
        File file = new File(path + "/input/" + mapName);
        System.out.println(file);
        if(file != null){
            controller.map = Loader.LoadMap(file);
            if(controller.map == null){
                // show popup or something
                Alert a = new Alert(Alert.AlertType.ERROR, "File contents not valid map.");
                a.showAndWait();
                return;
            }
            controller.loadMap();
        }
    }
}
