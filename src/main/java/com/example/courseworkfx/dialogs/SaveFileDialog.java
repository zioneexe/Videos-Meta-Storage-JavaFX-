package com.example.courseworkfx.dialogs;

import com.example.courseworkfx.VideoFile;
import javafx.application.Application;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFileDialog extends Application {

        private TableView<VideoFile> tableView;

        public SaveFileDialog(TableView<VideoFile> tableView) {
            this.tableView = tableView;
        }

        @Override
        public void start(Stage primaryStage) throws IOException {

            primaryStage.setTitle("Save the table.");

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.setInitialFileName("output.txt");

            java.io.File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                    if (tableView.getItems().isEmpty()) {
                        writer.write("Unlucky.. The table is empty!");
                    } else {
                        writer.write(tableView.getItems().toString());
                    }

                    writer.close();

                    System.out.println("Data has been written to: " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
