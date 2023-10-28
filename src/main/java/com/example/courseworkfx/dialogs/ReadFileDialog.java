package com.example.courseworkfx.dialogs;

import com.example.courseworkfx.Constants;
import com.example.courseworkfx.VideoFile;
import com.example.courseworkfx.VideoFileParser;
import com.example.courseworkfx.exceptions.InvalidFilenameException;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.example.courseworkfx.dialogs.InvalidFilenameAlert.showInvalidFilenameAlert;

public class ReadFileDialog extends Application implements Constants {

    private TableView<VideoFile> tableView;

    public ReadFileDialog(TableView<VideoFile> tableView) {
        this.tableView = tableView;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Read the table.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(CHOOSE_FILEPATH));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                ObservableList<VideoFile> fileList = tableView.getItems();

                line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    VideoFile parsedFile = VideoFileParser.parse(line);
                    if (parsedFile != null) fileList.add(parsedFile);
                }
                tableView.setItems(fileList);
            } catch (IOException e) {
                showInvalidFilenameAlert();
                throw new InvalidFilenameException();
            }
        }
    }

}
