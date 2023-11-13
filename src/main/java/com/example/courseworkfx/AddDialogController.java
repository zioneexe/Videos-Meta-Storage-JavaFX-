package com.example.courseworkfx;

import com.example.courseworkfx.exceptions.InvalidDataException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.example.courseworkfx.dialogs.InvalidDataAlert.showInvalidDataAlert;

/**
 * The `AddDialogController` class controls the behavior of the dialog for adding a new video file.
 * It implements the Initializable interface to initialize the controller after its root element has been completely processed.
 */
public class AddDialogController implements Constants {

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

    // instance of main controller class
    private CourseworkController courseworkController;

    /**
     * Sets the coursework controller associated with this dialog controller.
     *
     * @param courseworkController The coursework controller to set.
     */
    public void setCourseworkController(CourseworkController courseworkController) {
        this.courseworkController = courseworkController;
    }

    //------------------------------------------------------------------------------------------

    /**
     * Handles the event when the submit button is clicked.
     *
     * @throws Exception If an exception occurs during the submission process.
     */
    @FXML
    private void onSubmitClicked() throws Exception {
        try {
            String name = textFieldName.getText();
            String fileLocation = textFieldFileLocation.getText();
            String fileFormat = textFieldFormat.getText();
            String videoCodec = textFieldVCodec.getText();
            String audioCodec = textFieldACodec.getText();
            Boolean hasSubtitles = checkBoxSubtitles.isSelected();
            String player = textFieldPlayer.getText();

            Double fileDuration = parseDouble(textFieldDuration.getText());
            Double videoSize = parseDouble(textFieldSize.getText());

            // Check for empty fields
            if (name.isEmpty() || fileLocation.isEmpty() || fileFormat.isEmpty() ||
                    videoCodec.isEmpty() || audioCodec.isEmpty() || player.isEmpty()) {
                throw new InvalidDataException();
            }

            // Create a new VideoFile instance
            VideoFile videoFile = new VideoFile(name, fileLocation, fileFormat,
                    fileDuration, videoCodec, audioCodec, hasSubtitles, videoSize, player);

            // Add the VideoFile to the coursework controller if available
            if (courseworkController != null) courseworkController.addVideoFile(videoFile);

            // Close the dialog window
            Stage stage = (Stage) buttonSubmit.getScene().getWindow();
            stage.close();
        } catch (InvalidDataException e) {
            // Display an alert for invalid data and close the dialog window
            System.out.println(e.getMessage());
            showInvalidDataAlert();
            Stage stage = (Stage) buttonSubmit.getScene().getWindow();
            stage.close();
        }
    }

    //------------------------------------------------------------------------------------------

    /**
     * Parses a string input to a Double, handling invalid data exceptions.
     *
     * @param input The input string to be parsed.
     * @return The parsed Double value.
     * @throws InvalidDataException If the input cannot be parsed to a Double.
     */
    private Double parseDouble(String input) throws InvalidDataException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new InvalidDataException();
        }
    }

    /**
     * Handles the close request for the dialog.
     * It is triggered when the user attempts to close the dialog window.
     * Closes the dialog window, releasing associated resources.
     */
    void handleCloseRequest() {
        // Obtain a reference to the stage (window) containing the button
        Stage stage = (Stage) buttonSubmit.getScene().getWindow();

        // Close the dialog window
        stage.close();
    }
}
