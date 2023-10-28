package com.example.courseworkfx.dialogs;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class InvalidFilenameAlert extends Application {
    public static void showInvalidFilenameAlert() throws Exception {
        InvalidFilenameAlert alert = new InvalidFilenameAlert();
        Stage stage = new Stage();
        alert.start(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oooh..");
        alert.setContentText("The filename is incorrect...");
        alert.showAndWait();

    }
}