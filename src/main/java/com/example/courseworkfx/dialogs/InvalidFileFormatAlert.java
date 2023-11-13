package com.example.courseworkfx.dialogs;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * The `InvalidFileFormatAlert` class is responsible for displaying an alert when an invalid file format is detected.
 * It extends the JavaFX `Application` class and shows an error alert when started.
 */
public class InvalidFileFormatAlert extends Application {

    /**
     * Shows the invalid file format alert by launching a new instance of the `InvalidFileFormatAlert` class.
     *
     * @throws Exception If an exception occurs during the alert display.
     */
    public static void showInvalidFileFormatAlert() throws Exception {
        InvalidFileFormatAlert alert = new InvalidFileFormatAlert();
        Stage stage = new Stage();
        alert.start(stage);
    }

    /**
     * JavaFX Application start method.
     *
     * @param stage The primary stage for the application.
     * @throws Exception If an exception occurs during the alert display.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oooh..");
        alert.setContentText("The file format is incorrect! Search for .txt");
        alert.showAndWait();
    }
}
