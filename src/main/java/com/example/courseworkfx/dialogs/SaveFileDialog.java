package com.example.courseworkfx.dialogs;

import com.example.courseworkfx.VideoFile;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The `SaveFileDialog` class is responsible for saving the content of a TableView to a text file.
 * It extends the JavaFX `Application` class and provides a method to start the file saving process.
 */
public class SaveFileDialog extends Application {

    private TableView<VideoFile> tableView;

    /**
     * Constructs a new `SaveFileDialog` instance with the specified `tableView`.
     *
     * @param tableView The TableView whose content needs to be saved.
     */
    public SaveFileDialog(TableView<VideoFile> tableView) {
        this.tableView = tableView;
    }

    /**
     * JavaFX Application start method.
     *
     * @param primaryStage The primary stage for the application.
     * @throws IOException If an exception occurs during the file saving process.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Set the title of the primary stage
        primaryStage.setTitle("Save the table.");

        // Create a file chooser with a filter for text files and an initial file name
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("output.txt");

        // Show the save dialog and get the selected file
        java.io.File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                // Create a buffered writer for writing to the selected file
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                // Check if the table view is empty
                if (tableView.getItems().isEmpty()) {
                    // If the table is empty, write a message to the file
                    writer.write("Unlucky.. The table is empty!");
                } else {
                    // If the table is not empty, write the header and data to the file
                    writer.write("Name;File location;File format;Duration (HH:MM:SS);" +
                            "VCodec;ACodec;Has subtitles;Video size (MB);Player,\n");
                    for (VideoFile videoFile : tableView.getItems()) {
                        writer.write(videoFile.toString());
                    }
                }

                // Close the writer
                writer.close();

                // Print a message indicating that the data has been written to the file
                System.out.println("Data has been written to: " + file.getAbsolutePath());
            } catch (IOException e) {
                // Handle IOException by printing the stack trace
                e.printStackTrace();
            }
        } else {
            // If no file was selected, show an information alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("File Selection");
            alert.setHeaderText(null);
            alert.setContentText("File selection was canceled.");
            alert.showAndWait();
        }
    }
}
