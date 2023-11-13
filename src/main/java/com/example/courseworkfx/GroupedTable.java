package com.example.courseworkfx;

import com.example.courseworkfx.exceptions.EmptyTableException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;

import com.example.courseworkfx.CourseworkController.GroupingType;
import static com.example.courseworkfx.CourseworkController.shellSort;
import static com.example.courseworkfx.dialogs.EmptyTableAlert.showEmptyTableAlert;

/**
 * The `GroupedTable` class represents a JavaFX application to display and manipulate a grouped table of VideoFile objects.
 */
public class GroupedTable extends Application implements Constants {
    private ArrayList<TableView<VideoFile>> groupTables = new ArrayList<>();
    private ObservableList<VideoFile> videoFiles;

    MenuBar menuBar;
    Menu sortMenu;
    MenuItem menuSBDuration;
    MenuItem menuSBSize;
    MenuItem menuSBFormat;
    Menu calculateMenu;
    MenuItem menuCalcMaxSize;
    MenuItem menuCalcMinSize;
    Menu filterMenu;
    CheckMenuItem menuFBSubtitles;
    private GroupingType groupingType;

    /**
     * Constructor for `GroupedTable` class.
     * @param videoFiles The list of VideoFile objects to be displayed.
     * @param groupingType The grouping type used for categorizing the videos.
     */
    public GroupedTable(ObservableList<VideoFile> videoFiles, GroupingType groupingType) {
        this.videoFiles = videoFiles;
        this.groupingType = groupingType;
    }

    // Method to handle the action when subtitles are checked in the menu.
    void onMenuFBSubtitlesChecked() {
        boolean subtitlesChecked = menuFBSubtitles.isSelected();
        for (TableView<VideoFile> tableView : groupTables) {
            ObservableList<VideoFile> filteredList = FXCollections.observableArrayList();

            if (subtitlesChecked) {
                for (VideoFile videoFile : videoFiles) {
                    if (videoFile.ifHasSubtitles()) {
                        filteredList.add(videoFile);
                    }
                }
            } else {
                filteredList.addAll(videoFiles);
            }

            tableView.setItems(filteredList);
        }
    }

    // Method to handle the action when "Calculate Max Size" menu item is clicked.
    void onMenuCalcMaxSizeClicked() throws Exception {
        ArrayList<Double> resultsArray = new ArrayList<>();
        for (TableView<VideoFile> tableView : groupTables) {
            ObservableList<VideoFile> videos = tableView.getItems();
            try {
                if (videos.isEmpty()) {
                    showEmptyTableAlert();
                    throw new EmptyTableException();
                }
            } catch (EmptyTableException e) {
                return;
            }
            Double result = videos.get(0).getVideoSize();

            for (VideoFile file : videos) {
                if (file.getVideoSize() > result) result = file.getVideoSize();
            }
            resultsArray.add(result);
        }

        // Build a string representation of the calculated results.
        StringBuilder resultsString = new StringBuilder();
        for (int i = 0; i < resultsArray.size(); i++) {
            resultsString.append(resultsArray.get(i));
            if (i < resultsArray.size() - 1) {
                resultsString.append(", ");
            }
        }

        // Display an information dialog with the calculated result.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Maximum video file size.");
        alert.setContentText("The calculated result is: " + resultsString + " MBs");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);
        alert.showAndWait();
    }

    // Method to handle the action when "Calculate Min Size" menu item is clicked.
    void onMenuCalcMinSizeClicked() throws Exception {
        ArrayList<Double> resultsArray = new ArrayList<>();
        for (TableView<VideoFile> tableView : groupTables) {
            ObservableList<VideoFile> videos = tableView.getItems();
            try {
                if (videos.isEmpty()) {
                    showEmptyTableAlert();
                    throw new EmptyTableException();
                }
            } catch (EmptyTableException e) {
                System.out.println(e.getMessage());
                return;
            }
            Double result = videos.get(0).getVideoSize();

            for (VideoFile file : videos) {
                if (file.getVideoSize() < result) result = file.getVideoSize();
            }
            resultsArray.add(result);
        }

        // Build a string representation of the calculated results.
        StringBuilder resultsString = new StringBuilder();
        for (int i = 0; i < resultsArray.size(); i++) {
            resultsString.append(resultsArray.get(i));
            if (i < resultsArray.size() - 1) {
                resultsString.append(", ");
            }
        }

        // Display an information dialog with the calculated result.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Minimum video file size.");
        alert.setContentText("The calculated result is: " + resultsString + " MBs");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);
        alert.showAndWait();
    }

    // Method to handle the action when "Sort by Duration" menu item is clicked.
    void onMenuSBDurationClicked() {
        for (TableView<VideoFile> tableView : groupTables) {
            ObservableList<VideoFile> videos = tableView.getItems();
            shellSort(videos, Comparator.comparing(VideoFile::getFileDuration));
            tableView.setItems(videos);
        }
    }

    // Method to handle the action when "Sort by Size" menu item is clicked.
    void onMenuSBSizeClicked() {
        for (TableView<VideoFile> tableView : groupTables) {
            ObservableList<VideoFile> videos = tableView.getItems();
            shellSort(videos, Comparator.comparing(VideoFile::getVideoSize));
            tableView.setItems(videos);
        }
    }

    // Method to handle the action when "Sort by Format" menu item is clicked.
    void onMenuSBFormatClicked() {
        for (TableView<VideoFile> tableView : groupTables) {
            ObservableList<VideoFile> videos = tableView.getItems();
            shellSort(videos, Comparator.comparing(VideoFile::getFileFormat));
            tableView.setItems(videos);
        }
    }

    // JavaFX Application start method.
    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Grouped Table");
        stage.getIcons().add(new Image(ICON_FILEPATH));

        VBox vbox = new VBox();

        menuBar = new MenuBar();
        sortMenu = new Menu("Sort by");
        menuSBDuration = new MenuItem("Duration");
        menuSBSize = new MenuItem("Size");
        menuSBFormat = new MenuItem("Format");
        calculateMenu = new Menu("Calculate");
        menuCalcMaxSize = new MenuItem("Max size");
        menuCalcMinSize = new MenuItem("Min size");
        filterMenu = new Menu("Filter by");
        menuFBSubtitles = new CheckMenuItem("Subtitles");

        sortMenu.getItems().addAll(menuSBDuration, menuSBSize, menuSBFormat);
        calculateMenu.getItems().addAll(menuCalcMaxSize, menuCalcMinSize);
        filterMenu.getItems().add(menuFBSubtitles);
        menuBar.getMenus().addAll(sortMenu, calculateMenu, filterMenu);

        // Set action event handlers for menu items.
        menuSBDuration.setOnAction(e -> onMenuSBDurationClicked());
        menuSBSize.setOnAction(e -> onMenuSBSizeClicked());
        menuSBFormat.setOnAction(e -> onMenuSBFormatClicked());
        menuCalcMaxSize.setOnAction(e -> {
            try {
                onMenuCalcMaxSizeClicked();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        menuCalcMinSize.setOnAction(e -> {
            try {
                onMenuCalcMinSizeClicked();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        menuFBSubtitles.setOnAction(e -> onMenuFBSubtitlesChecked());

        vbox.getChildren().add(menuBar);

        // Define TableColumn instances for the TableView.
        TableColumn<VideoFile, String> nameColumn = new TableColumn<>("Video");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<VideoFile, String> pathColumn = new TableColumn<>("Path");
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("fileLocation"));

        TableColumn<VideoFile, String> formatColumn = new TableColumn<>("Format");
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("fileFormat"));

        TableColumn<VideoFile, Double> durationColumn = new TableColumn<>("Duration (mins)");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("fileDuration"));

        TableColumn<VideoFile, String> vcodecColumn = new TableColumn<>("Video Codec");
        vcodecColumn.setCellValueFactory(new PropertyValueFactory<>("videoCodec"));

        TableColumn<VideoFile, String> acodecColumn = new TableColumn<>("Audio Codec");
        acodecColumn.setCellValueFactory(new PropertyValueFactory<>("audioCodec"));

        TableColumn<VideoFile, Boolean> subtitlesColumn = new TableColumn<>("Subtitles");
        subtitlesColumn.setCellValueFactory(cellData -> cellData.getValue().hasSubtitlesProperty());
        subtitlesColumn.setCellFactory(CheckBoxTableCell.forTableColumn(subtitlesColumn));

        TableColumn<VideoFile, Double> sizeColumn = new TableColumn<>("Size (MB)");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("videoSize"));

        TableColumn<VideoFile, String> playerColumn = new TableColumn<>("Player");
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));

        // ObservableList to store the unique categories based on groupingType.
        ObservableList<String> category = FXCollections.observableArrayList();

        // Populate the category list based on the grouping type.
        switch (groupingType) {
            case AUDIO_CODEC:
                for (VideoFile videoFile : videoFiles) {
                    String audioCodec = videoFile.getAudioCodec();
                    if (!category.contains(audioCodec)) {
                        category.add(audioCodec);
                    }
                }
                break;
            case VIDEO_CODEC:
                for (VideoFile videoFile : videoFiles) {
                    String videoCodec = videoFile.getVideoCodec();
                    if (!category.contains(videoCodec)) {
                        category.add(videoCodec);
                    }
                }
                break;
            case PLAYER:
                for (VideoFile videoFile : videoFiles) {
                    String player = videoFile.getPlayer();
                    if (!category.contains(player)) {
                        category.add(player);
                    }
                }
                break;
            case LONGEST_VIDEOS:
                break;
            default:
                throw new RuntimeException("Invalid grouping mode.");
        }

        // Create TableView instances for each category and add them to the VBox.
        for (String item : category) {
            TableView<VideoFile> tableOfGroup = new TableView<>();
            tableOfGroup.setPrefWidth(800);
            tableOfGroup.setPrefHeight(135);

            // Label for displaying the category name.
            Label categoryLabel = new Label(item);
            categoryLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            vbox.getChildren().add(categoryLabel);

            // ObservableList to store VideoFile objects for the current category.
            ObservableList<VideoFile> groupVideoFiles = FXCollections.observableArrayList();

            // Populate the groupVideoFiles based on the grouping type.
            switch (groupingType) {
                case AUDIO_CODEC:
                    for (VideoFile videoFile : videoFiles) {
                        if (videoFile.getAudioCodec().equals(item)) {
                            groupVideoFiles.add(videoFile);
                        }
                    }
                    break;
                case VIDEO_CODEC:
                    for (VideoFile videoFile : videoFiles) {
                        if (videoFile.getVideoCodec().equals(item)) {
                            groupVideoFiles.add(videoFile);
                        }
                    }
                    break;
                case PLAYER:
                    for (VideoFile videoFile : videoFiles) {
                        if (videoFile.getPlayer().equals(item)) {
                            groupVideoFiles.add(videoFile);
                        }
                    }
                    break;
                default:
                    throw new RuntimeException("Invalid grouping mode.");
            }

            // Set the columns and items for the current TableView.
            tableOfGroup.getColumns().addAll(nameColumn, pathColumn, formatColumn, durationColumn,
                    vcodecColumn, acodecColumn, subtitlesColumn, sizeColumn, playerColumn);
            tableOfGroup.setItems(groupVideoFiles);

            // Add the TableView to the VBox and the list of groupTables.
            vbox.getChildren().add(tableOfGroup);
            groupTables.add(tableOfGroup);
        }

        // Special case for groupingType LONGEST_VIDEOS.
        if (groupingType == GroupingType.LONGEST_VIDEOS) {
            TableView<VideoFile> longestVideosTable = new TableView<>();
            longestVideosTable.setPrefWidth(800);
            longestVideosTable.setPrefHeight(135);
            ObservableList<VideoFile> filteredList = FXCollections.observableArrayList();

            // Find the maximum duration among all videos.
            double maxDuration = videoFiles.stream()
                    .mapToDouble(VideoFile::getFileDuration)
                    .max()
                    .orElse(0);

            // Filter videos with the maximum duration.
            filteredList.addAll(videoFiles.stream()
                    .filter(videoFile -> videoFile.getFileDuration() == maxDuration)
                    .toList());
            videoFiles = filteredList;

            // Set columns and items for the TableView of longest videos.
            longestVideosTable.getColumns().addAll(nameColumn, pathColumn, formatColumn, durationColumn,
                    vcodecColumn, acodecColumn, subtitlesColumn, sizeColumn, playerColumn);
            longestVideosTable.setItems(videoFiles);

            // Add the TableView to the VBox and the list of groupTables.
            vbox.getChildren().add(longestVideosTable);
            groupTables.add(longestVideosTable);
        }

        // Create the scene and set it to the stage.
        Scene scene = new Scene(vbox);
        stage.setScene(scene);

        // Show the stage.
        stage.show();
    }
}
