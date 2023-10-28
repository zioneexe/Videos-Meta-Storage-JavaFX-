package com.example.courseworkfx.dialogs;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class EmptySelectionAlert extends Application {
    public static void showEmptyTableAlert() throws Exception {
        EmptySelectionAlert alert = new EmptySelectionAlert();
        Stage stage = new Stage();
        alert.start(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oooh..");
        alert.setContentText("It seems that no item is selected.");
        alert.showAndWait();

    }
}
