package com.example.courseworkfx.dialogs;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class InvalidDataAlert extends Application {
    public static void showInvalidDataAlert() throws Exception {
        InvalidDataAlert alert = new InvalidDataAlert();
        Stage stage = new Stage();
        alert.start(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oooh..");
        alert.setContentText("Your input is unacceptable!");
        alert.showAndWait();
    }
}
