package com.example.courseworkfx.dialogs;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * The `EmptyTableAlert` class is responsible for displaying an alert when attempting an action on an empty table.
 * It extends the JavaFX `Application` class and shows an error alert when started.
 */
public class EmptyTableAlert extends Application {

    /**
     * Shows the empty table alert by launching a new instance of the `EmptyTableAlert` class.
     *
     * @throws Exception If an exception occurs during the alert display.
     */
    public static void showEmptyTableAlert() throws Exception {
        EmptyTableAlert alert = new EmptyTableAlert();
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
        alert.setContentText("You can't do that! The table is empty.");
        alert.showAndWait();
    }
}
