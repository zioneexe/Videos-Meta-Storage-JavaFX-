package com.example.courseworkfx.dialogs;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * The `CancelDialogAlert` class is responsible for displaying an alert when canceling read/write file dialog.
 * It extends the JavaFX `Application` class and shows an error alert when started.
 */
public class CancelDialogAlert extends Application {

    /**
     * Shows the empty table alert by launching a new instance of the `CancelDialogAlert` class.
     *
     * @throws Exception If an exception occurs during the alert display.
     */
    public static void showCancelDialogAlert() throws Exception {
        CancelDialogAlert alert = new CancelDialogAlert();
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("File Selection");
        alert.setHeaderText(null);
        alert.setContentText("File selection was canceled.");
        alert.showAndWait();
    }
}