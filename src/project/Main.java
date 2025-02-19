/*
soubor: project.Main.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: třída Main, spouštějící hlavní okno a načítaní defaultní mapy
 */

package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import project.map.Map;

import java.io.File;

/**
 * main class opening and initializing main window
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        Controller c = loader.getController();
        Scene scene = new Scene(root, 1280, 720);

        // window settings
        primaryStage.setTitle("IJA Map");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(360);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.setOnHidden(e -> c.close());
        primaryStage.show();

        //default map loading
        loadDefaultMap("novigrad.json", c);
    }

    /**
     * launches application with arguments
     * @param args launch arguents
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * loads default map data
     * @param mapName name of default map
     * @param c controller data
     */
    public void loadDefaultMap(String mapName, Controller c){
        String path = System.getProperty("user.dir");
        File file = new File(path + "/input/" + mapName);
        Map m = Loader.LoadMap(file);
        if(m == null){
            return;
        }
        c.map = m;
        c.loadMap();
    }
}
