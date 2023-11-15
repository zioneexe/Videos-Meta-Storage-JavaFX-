package com.example.courseworkfx.dialogs;

import com.example.courseworkfx.Constants;
import com.example.courseworkfx.VideoFile;
import com.example.courseworkfx.VideoFileParser;
import com.example.courseworkfx.exceptions.InvalidFileFormatException;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.example.courseworkfx.dialogs.CancelDialogAlert.showCancelDialogAlert;
import static com.example.courseworkfx.dialogs.InvalidFileFormatAlert.showInvalidFileFormatAlert;

/**
 * The `ReadFileDialog` class is responsible for reading a file and populating a TableView with the data.
 * It extends the JavaFX `Application` class and provides a method to start the file reading process.
 */
public class ReadFileDialog extends Application implements Constants {

    // Table instance
    private final TableView<VideoFile> TABLE_VIEW;

    /**
     * Constructs a new `ReadFileDialog` instance with the specified `tableView`.
     *
     * @param tableView The TableView to populate with the read data.
     */
    public ReadFileDialog(TableView<VideoFile> tableView) {
        this.TABLE_VIEW = tableView;
    }

    //------------------------------------------------------------------------------------------

    /**
     * JavaFX Application start method.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception If an exception occurs during the file reading process.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Read the table.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(CHOOSE_FILEPATH));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            try {
                String fileName = file.getName();
                if (!fileName.toLowerCase().endsWith(".txt")) {
                    showInvalidFileFormatAlert();
                    throw new InvalidFileFormatException();
                }
            } catch (InvalidFileFormatException e) {
                System.out.println(e.getMessage());
                return;
            }
            readFromFileAndPopulateTable(file);
        } else {
            showCancelDialogAlert();
        }
    }

    /**
     * Reads data from the specified file and populates the associated TableView with the parsed VideoFile objects.
     *
     * @param file The File object from which to read data.
     * @throws RuntimeException If an IOException occurs during the file reading process.
     */
    private void readFromFileAndPopulateTable(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            ObservableList<VideoFile> fileList = TABLE_VIEW.getItems();

            // Skip the header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                VideoFile parsedFile = VideoFileParser.parse(line);
                if (parsedFile != null) {
                    fileList.add(parsedFile);
                }
            }
            TABLE_VIEW.setItems(fileList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
