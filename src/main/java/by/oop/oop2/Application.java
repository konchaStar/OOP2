package by.oop.oop2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Application extends javafx.application.Application {
    public static Stage stStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("GUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 349, 400);
        stage.setTitle("Editor");
        stage.setScene(scene);
        stage.setOnCloseRequest(x -> {
            System.exit(0);
        });
        stStage = stage;
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}