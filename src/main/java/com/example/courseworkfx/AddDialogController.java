package com.example.courseworkfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.courseworkfx.dialogs.InvalidDataAlert.showInvalidDataAlert;

public class AddDialogController implements Initializable, Constants {

    @FXML
    private Button buttonSubmit;

    @FXML
    private CheckBox checkBoxSubtitles;

    @FXML
    private TextField textFieldACodec;

    @FXML
    private TextField textFieldDuration;

    @FXML
    private TextField textFieldFileLocation;

    @FXML
    private TextField textFieldFormat;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldPlayer;

    @FXML
    private TextField textFieldSize;

    @FXML
    private TextField textFieldVCodec;
    private CourseworkController courseworkController;

    public void setCourseworkController(CourseworkController courseworkController) {
        this.courseworkController = courseworkController;
    }
    @FXML
    private void onSubmitClicked() throws Exception {
        try {
            String name = textFieldName.getText();
            String fileLocation = textFieldFileLocation.getText();
            String fileFormat = textFieldFormat.getText();
            Double fileDuration = parseDouble(textFieldDuration.getText());
            String videoCodec = textFieldVCodec.getText();
            String audioCodec = textFieldACodec.getText();
            Boolean hasSubtitles = checkBoxSubtitles.isSelected();
            Double videoSize = parseDouble(textFieldSize.getText());
            String player = textFieldPlayer.getText();

            if (name.isEmpty() || fileLocation.isEmpty() || fileFormat.isEmpty() ||
                    videoCodec.isEmpty() || audioCodec.isEmpty() || player.isEmpty()) {
                showInvalidDataAlert();
                return;
            }

            VideoFile videoFile = new VideoFile(name, fileLocation, fileFormat,
                    fileDuration, videoCodec, audioCodec, hasSubtitles, videoSize, player);

            if (courseworkController != null) courseworkController.addVideoFile(videoFile);

            Stage stage = (Stage) buttonSubmit.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            Stage stage = (Stage) buttonSubmit.getScene().getWindow();
            stage.close();
        }
    }

    private Double parseDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid numeric input: [ " + input + " ]");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("coursework-view.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setCourseworkController(fxmlLoader.getController());
    }
}
