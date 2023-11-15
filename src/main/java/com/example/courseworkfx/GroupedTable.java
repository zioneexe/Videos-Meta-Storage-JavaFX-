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

    // ArrayList to store grouped tables
    private final ArrayList<TableView<VideoFile>> GROUP_TABLES = new ArrayList<>();

    // ArrayList of initial videos of each table
    private final ArrayList<ObservableList<VideoFile>> SAVED_VIDEOS = new ArrayList<>();

    // Observable list to store video files
    private ObservableList<VideoFile> videoFiles;

    // ArrayList of grouping categories
    private ArrayList<String> categories = new ArrayList<>();

    // Current stage
    private Stage stage;

    // Main scene component
    private VBox vbox;

    // Menu components
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

    // Table columns
    TableColumn<VideoFile, String> nameColumn;

    TableColumn<VideoFile, String> pathColumn;

    TableColumn<VideoFile, String> formatColumn;

    TableColumn<VideoFile, Double> durationColumn;

    TableColumn<VideoFile, String> vcodecColumn;

    TableColumn<VideoFile, String> acodecColumn;

    TableColumn<VideoFile, Boolean> subtitlesColumn;


    TableColumn<VideoFile, Double> sizeColumn;


    TableColumn<VideoFile, String> playerColumn;


    // Grouping type for the tables
    private final GroupingType GROUPING_TYPE;

    /**
     * Constructor for `GroupedTable` class.
     *
     * @param videoFiles   The list of VideoFile objects to be displayed.
     * @param groupingType The grouping type used for categorizing the videos.
     */
    public GroupedTable(ObservableList<VideoFile> videoFiles, GroupingType groupingType) {
        this.videoFiles = videoFiles;
        this.GROUPING_TYPE = groupingType;
    }

    /**
     * Method to handle the action when subtitles are checked in the menu.
     */
    void onMenuFBSubtitlesChecked() {
        boolean subtitlesChecked = menuFBSubtitles.isSelected();
        for (int i = 0; i < GROUP_TABLES.size(); ++i) {
            TableView<VideoFile> tableView = GROUP_TABLES.get(i);
            ObservableList<VideoFile> tableVideos = subtitlesChecked ? tableView.getItems() : SAVED_VIDEOS.get(i);
            checkGroupForSubtitles(subtitlesChecked, tableView, tableVideos);
        }
    }

    /**
     * Static method to check the group for subtitles and update the TableView accordingly.
     *
     * @param subtitlesChecked Boolean indicating whether subtitles are checked.
     * @param tableView        The TableView to be updated.
     * @param videoFiles       The list of VideoFile objects.
     */
    static void checkGroupForSubtitles(boolean subtitlesChecked, TableView<VideoFile> tableView,
                                       ObservableList<VideoFile> videoFiles) {
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

    /**
     * Method to handle the action when "Calculate Max Size" menu item is clicked.
     *
     * @throws Exception If an exception occurs during the calculation.
     */
    void onMenuCalcMaxSizeClicked() throws Exception {
        ArrayList<Double> resultsArray = new ArrayList<>();
        for (TableView<VideoFile> tableView : GROUP_TABLES) {
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
        Alert alert = getAlert(resultsArray, "Maximum video file size.");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Private static method to create an alert with calculated results.
     *
     * @param resultsArray The list of calculated results.
     * @param titleString  The title for the alert.
     * @return An Alert object.
     */
    private static Alert getAlert(ArrayList<Double> resultsArray, String titleString) {
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
        alert.setHeaderText(titleString);
        alert.setContentText("The calculated result is: " + resultsString + " MBs");
        return alert;
    }

    /**
     * Method to handle the action when "Calculate Min Size" menu item is clicked.
     *
     * @throws Exception If an exception occurs during the calculation.
     */
    void onMenuCalcMinSizeClicked() throws Exception {
        ArrayList<Double> resultsArray = new ArrayList<>();
        for (TableView<VideoFile> tableView : GROUP_TABLES) {
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
        Alert alert = getAlert(resultsArray, "Minimum video file size.");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Method to handle the action when "Sort by Duration" menu item is clicked.
     */
    void onMenuSBDurationClicked() {
        for (TableView<VideoFile> tableView : GROUP_TABLES) {
            ObservableList<VideoFile> videos = tableView.getItems();
            ObservableList<VideoFile> sortedVideos = shellSort(videos, Comparator.comparing(VideoFile::getFileDuration));
            tableView.setItems(sortedVideos);
        }
    }

    /**
     * Method to handle the action when "Sort by Size" menu item is clicked.
     */
    void onMenuSBSizeClicked() {
        for (TableView<VideoFile> tableView : GROUP_TABLES) {
            ObservableList<VideoFile> videos = tableView.getItems();
            ObservableList<VideoFile> sortedVideos = shellSort(videos, Comparator.comparing(VideoFile::getVideoSize));
            tableView.setItems(sortedVideos);
        }
    }

    /**
     * Method to handle the action when "Sort by Format" menu item is clicked.
     */
    void onMenuSBFormatClicked() {
        for (TableView<VideoFile> tableView : GROUP_TABLES) {
            ObservableList<VideoFile> videos = tableView.getItems();
            ObservableList<VideoFile> sortedVideos = shellSort(videos, Comparator.comparing(VideoFile::getFileFormat));
            tableView.setItems(sortedVideos);
        }
    }

    //------------------------------------------------------------------------------------------

    /**
     * JavaFX grouped table window start method.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception If an exception occurs during the application start.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        vbox = new VBox();

        setStage();
        setMenu();
        setColumnValueFactories();

        // Populate the category list based on the grouping type.
        formCategoriesList();
        // Create TableView instances for each category and add them to the VBox.
        for (String category : categories) {
            populateCategoryTable(category);
        }

        // Special case for groupingType LONGEST_VIDEOS.
        if (GROUPING_TYPE == GroupingType.LONGEST_VIDEOS) {
            createLongestVideosTable();
        }

        // Create the scene and set it to the stage.
        Scene scene = new Scene(vbox);
        stage.setScene(scene);

        // Show the stage.
        stage.show();
    }

    /**
     * Sets up the stage for the application.
     * Configures the title and adds an icon to the stage.
     */
    private void setStage() {
        stage = new Stage();
        stage.setTitle("Grouped Table");
        stage.getIcons().add(new Image(ICON_FILEPATH));
    }

    /**
     * Sets up TableColumn instances for the TableView and associates them with specific properties of the VideoFile class.
     * Each TableColumn represents a different attribute of a video file.
     */
    private void setColumnValueFactories() {
        nameColumn = new TableColumn<>("Video");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        pathColumn = new TableColumn<>("Path");
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("fileLocation"));

        formatColumn = new TableColumn<>("Format");
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("fileFormat"));

        durationColumn = new TableColumn<>("Duration (mins)");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("fileDuration"));

        vcodecColumn = new TableColumn<>("Video Codec");
        vcodecColumn.setCellValueFactory(new PropertyValueFactory<>("videoCodec"));

        acodecColumn = new TableColumn<>("Audio Codec");
        acodecColumn.setCellValueFactory(new PropertyValueFactory<>("audioCodec"));

        subtitlesColumn = new TableColumn<>("Subtitles");
        subtitlesColumn.setCellValueFactory(cellData -> cellData.getValue().hasSubtitlesProperty());
        subtitlesColumn.setCellFactory(CheckBoxTableCell.forTableColumn(subtitlesColumn));

        sizeColumn = new TableColumn<>("Size (MB)");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("videoSize"));

        playerColumn = new TableColumn<>("Player");
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
    }

    /**
     * Sets up the MenuBar, Menu, and MenuItems for the application.
     * Configures action event handlers for menu items.
     */
    private void setMenu() {
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
    }

    /**
     * Forms a list of categories based on the specified grouping type.
     * The categories are determined from the attributes of the VideoFile class.
     */
    private void formCategoriesList() {
        switch (GROUPING_TYPE) {
            case FORMAT:
                for (VideoFile videoFile : videoFiles) {
                    String fileFormat = videoFile.getFileFormat();
                    if (!categories.contains(fileFormat)) {
                        categories.add(fileFormat);
                    }
                }
                break;
            case AUDIO_CODEC:
                for (VideoFile videoFile : videoFiles) {
                    String audioCodec = videoFile.getAudioCodec();
                    if (!categories.contains(audioCodec)) {
                        categories.add(audioCodec);
                    }
                }
                break;
            case VIDEO_CODEC:
                for (VideoFile videoFile : videoFiles) {
                    String videoCodec = videoFile.getVideoCodec();
                    if (!categories.contains(videoCodec)) {
                        categories.add(videoCodec);
                    }
                }
                break;
            case PLAYER:
                for (VideoFile videoFile : videoFiles) {
                    String player = videoFile.getPlayer();
                    if (!categories.contains(player)) {
                        categories.add(player);
                    }
                }
                break;
            case LONGEST_VIDEOS:
                // Not handling this case for forming categories.
                break;
            default:
                throw new RuntimeException("Invalid grouping mode.");
        }
    }

    /**
     * Populates a TableView with VideoFile objects for a specific category.
     * The category can be either a file format, audio codec, video codec, or player.
     *
     * @param category The category for which to populate the TableView.
     */
    private void populateCategoryTable(String category) {
        VBox categoryVBox = new VBox();
        TableView<VideoFile> tableOfGroup = new TableView<>();

        setColumnValueFactories();

        tableOfGroup.getColumns().addAll(nameColumn, pathColumn, formatColumn, durationColumn,
                vcodecColumn, acodecColumn, subtitlesColumn, sizeColumn, playerColumn);
        tableOfGroup.setPrefWidth(850);
        tableOfGroup.setPrefHeight(135);

        // Label for displaying the category name.
        Label categoryLabel = new Label(category);
        categoryLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        // ObservableList to store VideoFile objects for the current category.
        ObservableList<VideoFile> groupVideoFiles;

        // Populate the groupVideoFiles based on the grouping type.
        switch (GROUPING_TYPE) {
            case FORMAT:
                groupVideoFiles = videoFiles.filtered(videoFile -> videoFile.getFileFormat().equals(category));
                break;
            case AUDIO_CODEC:
                groupVideoFiles = videoFiles.filtered(videoFile -> videoFile.getAudioCodec().equals(category));
                break;
            case VIDEO_CODEC:
                groupVideoFiles = videoFiles.filtered(videoFile -> videoFile.getVideoCodec().equals(category));
                break;
            case PLAYER:
                groupVideoFiles = videoFiles.filtered(videoFile -> videoFile.getPlayer().equals(category));
                break;
            default:
                throw new RuntimeException("Invalid grouping mode.");
        }

        // Set the columns and items for the current TableView.
        tableOfGroup.setItems(groupVideoFiles);

        // Add the TableView to the VBox and the list of groupTables.
        GROUP_TABLES.add(tableOfGroup);
        SAVED_VIDEOS.add(tableOfGroup.getItems());
        categoryVBox.getChildren().addAll(categoryLabel, tableOfGroup);
        vbox.getChildren().add(categoryVBox);
    }

    /**
     * Creates a TableView for the longest videos based on the file duration.
     * The TableView displays videos with the maximum duration.
     */
    private void createLongestVideosTable() {
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

        setColumnValueFactories();

        // Set columns and items for the TableView of longest videos.
        longestVideosTable.getColumns().addAll(nameColumn, pathColumn, formatColumn, durationColumn,
                vcodecColumn, acodecColumn, subtitlesColumn, sizeColumn, playerColumn);
        longestVideosTable.setItems(filteredList);

        // Add the TableView to the VBox and the list of groupTables.
        vbox.getChildren().add(longestVideosTable);
        GROUP_TABLES.add(longestVideosTable);
        SAVED_VIDEOS.add(longestVideosTable.getItems());
    }

    /**
     * Handles the close request for the dialog.
     * It is triggered when the user attempts to close the dialog window.
     * Closes the dialog window, releasing associated resources.
     */
    void handleCloseRequest() {
        // Obtain a reference to the stage (window) containing the button
        Stage stage = (Stage) GROUP_TABLES.get(0).getScene().getWindow();

        // Close the dialog window
        stage.close();
    }
}
