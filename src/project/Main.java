package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Controller c = loader.getController();
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 720);

        // window settings
        primaryStage.setTitle("IJA Map");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(360);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.setOnHidden(e -> {
            c.close();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
