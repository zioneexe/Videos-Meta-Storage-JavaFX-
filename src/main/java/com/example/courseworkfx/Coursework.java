package com.example.courseworkfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The `Coursework` class is the main entry point for the VideoFile DB application.
 * It extends the JavaFX `Application` class and is responsible for launching the JavaFX application.
 */
public class Coursework extends Application implements Constants {

    /**
     * JavaFX Application start method.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file and create a scene.
        FXMLLoader fxmlLoader = new FXMLLoader(Coursework.class.getResource("coursework-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), APP_WIDTH, APP_HEIGHT);
        Path stylesPath = Paths.get(STYLES_PATH);
        scene.getStylesheets().add(stylesPath.toUri().toString());

        // Configure the primary stage.
        stage.setTitle("VideoFile DB");
        stage.getIcons().add(new Image(ICON_FILEPATH));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method of the application, used to launch the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
