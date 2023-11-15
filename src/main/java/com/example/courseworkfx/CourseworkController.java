package com.example.courseworkfx;

import com.example.courseworkfx.dialogs.ReadFileDialog;
import com.example.courseworkfx.dialogs.SaveFileDialog;
import com.example.courseworkfx.exceptions.EmptyTableException;
import com.example.courseworkfx.exceptions.InvalidDataException;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.courseworkfx.dialogs.EmptyTableAlert.showEmptyTableAlert;
import static com.example.courseworkfx.dialogs.InvalidDataAlert.showInvalidDataAlert;

/**
 * Controller class for the main application window.
 */
public class CourseworkController implements Initializable, Constants {

    /**
     * Enumeration for filtering options
     */
    public enum FilterType {
        NAME,
        DURATION,
        FORMAT,
        PATH,
    }

    /**
     * Enumeration for grouping options
     */

    public enum GroupingType {
        FORMAT,
        AUDIO_CODEC,
        VIDEO_CODEC,
        PLAYER,
        LONGEST_VIDEOS
    }

    //------------------------------------------------------------------------------------------

    // Sidebar transition instance
    private TranslateTransition sidebarTransition;

    // FXML elements injected by the FXMLLoader

    @FXML
    private VBox sidebar;

    @FXML
    private TextField filterTextField;

    @FXML
    private ImageView buttonAdd;

    @FXML
    private ImageView buttonDelete;

    @FXML
    private ImageView buttonExit;

    @FXML
    private ImageView buttonHelp;

    @FXML
    private ImageView buttonRead;

    @FXML
    private ImageView buttonWrite;

    @FXML
    private MenuItem menuAbout;

    @FXML
    private MenuItem menuCalcAvgDuration;

    @FXML
    private MenuItem menuCalcAvgSize;

    @FXML
    private MenuItem menuCalcMaxDuration;

    @FXML
    private MenuItem menuCalcMaxSize;

    @FXML
    private MenuItem menuCalcMinSize;

    @FXML
    private MenuItem menuFBDuration;

    @FXML
    private MenuItem menuFBFormat;

    @FXML
    private MenuItem menuFBName;

    @FXML
    private MenuItem menuFBPath;

    @FXML
    private CheckMenuItem menuFBSubtitles;

    @FXML
    private MenuItem menuGBAudioCodec;

    @FXML
    private MenuItem menuGBFormat;

    @FXML
    private MenuItem menuGBLongestVideos;

    @FXML
    private MenuItem menuGBPlayer;

    @FXML
    private MenuItem menuGBVideoCodec;

    @FXML
    private MenuItem menuRead;

    @FXML
    private MenuItem menuSBDuration;

    @FXML
    private MenuItem menuSBFormat;

    @FXML
    private MenuItem menuSBSize;

    @FXML
    private MenuItem menuSave;

    @FXML
    private MenuItem menuVideosAdd;

    @FXML
    private MenuItem menuVideosRemove;

    @FXML
    private TableView<VideoFile> tableView;

    @FXML
    private TableColumn<VideoFile, String> nameColumn;

    @FXML
    private TableColumn<VideoFile, String> pathColumn;

    @FXML
    private TableColumn<VideoFile, String> playerColumn;

    @FXML
    private TableColumn<VideoFile, Double> sizeColumn;

    @FXML
    private TableColumn<VideoFile, Boolean> subtitlesColumn;

    @FXML
    private TableColumn<VideoFile, String> vcodecColumn;

    @FXML
    private TableColumn<VideoFile, String> acodecColumn;

    @FXML
    private TableColumn<VideoFile, Double> durationColumn;

    @FXML
    private TableColumn<VideoFile, String> formatColumn;

    // Other class fields
    private ObservableList<VideoFile> dataList;

    private FilterType currentFilter = FilterType.NAME;

    private VideoFile selectedFile;

    //------------------------------------------------------------------------------------------

    // Methods annotated with @FXML handle various UI events

    @FXML
    private void toggleSidebar() {
        // Function for hiding/showing menu sidebar
        if (sidebarTransition.getStatus() == Animation.Status.RUNNING) {
            // If the transition is currently running, do nothing
            return;
        }

        double sidebarWidth = sidebar.getWidth();
        double targetTranslateX = (sidebar.getTranslateX() == sidebarWidth + 142) ? 0 : sidebarWidth + 142;

        sidebarTransition.setFromX(sidebar.getTranslateX());
        sidebarTransition.setToX(targetTranslateX);
        sidebarTransition.play();
    }

    @FXML
    void onMenuAboutClicked(ActionEvent event) {
        // Open the documentation when the "About" menu item is clicked
        openDocumentation();
    }

    @FXML
    void onImageAddClicked(MouseEvent event) {
        // Handle the event when the "Add" image is clicked
        addVideo();
    }

    @FXML
    void onImageDeleteClicked(MouseEvent event) throws Exception {
        // Handle the event when the "Delete" image is clicked
        deleteVideo();
    }

    @FXML
    void onImageExitClicked(MouseEvent event) {
        // Handle the event when the "Exit" image is clicked
        // Display a warning dialog and prompt the user to save data before exiting
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Save the data before exiting the app!");

        callSaveDialog(alert);
        // Close the application window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void onImageHelpClicked(MouseEvent event) {
        // Open the documentation when the "Help" image is clicked
        openDocumentation();
    }

    @FXML
    void onImageReadClicked(MouseEvent event) throws Exception {
        // Handle the event when the "Read" image is clicked
        readFromFile();
    }

    @FXML
    void onImageWriteClicked(MouseEvent event) throws Exception {
        // Handle the event when the "Write" image is clicked
        writeToFile();
    }

    @FXML
    void onMenuCalcAvgDurationClicked(ActionEvent event) throws Exception {
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
        Double result = 0.0;

        for (VideoFile file : videos) {
            result += file.getFileDuration();
        }
        result /= videos.size();

        showAlert("Average video file duration.",
                "The calculated result is: " + String.format(Locale.US, "%.3f", result) + " min(s)");
    }

    @FXML
    void onMenuCalcAvgSizeClicked(ActionEvent event) throws Exception {
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
        Double result = 0.0;

        for (VideoFile file : videos) {
            result += file.getVideoSize();
        }
        result /= videos.size();

        showAlert("Average video file size.",
                "The calculated result is: " + String.format(Locale.US, "%.3f", result) + " MB");
    }

    @FXML
    void onMenuCalcMaxDurationClicked(ActionEvent event) throws Exception {
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
        Double result = videos.get(0).getFileDuration();

        for (VideoFile file : videos) {
            if (file.getFileDuration() > result) result = file.getFileDuration();
        }

        showAlert("Maximum video file duration.",
                "The calculated result is: " + result + " min(s)");
    }

    @FXML
    void onMenuCalcMaxSizeClicked(ActionEvent event) throws Exception {
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

        showAlert("Maximum video file size.",
                "The calculated result is: " + result + " MB");
    }

    @FXML
    void onMenuCalcMinSizeClicked(ActionEvent event) throws Exception {
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

        showAlert("Minimum video file size.",
                "The calculated result is: " + result + " MB");
    }

    @FXML
    void onMenuFBDurationClicked(ActionEvent event) {
        // Set the current filter to DURATION and apply the filter
        currentFilter = FilterType.DURATION;
        filterData(filterTextField.getText());
    }

    @FXML
    void onMenuFBFormatClicked(ActionEvent event) {
        // Set the current filter to FORMAT and apply the filter
        currentFilter = FilterType.FORMAT;
        filterData(filterTextField.getText());
    }

    @FXML
    void onMenuFBPathClicked(ActionEvent event) {
        // Set the current filter to PATH and apply the filter
        currentFilter = FilterType.PATH;
        filterData(filterTextField.getText());
    }

    @FXML
    void onMenuFBNameClicked(ActionEvent event) {
        // Set the current filter to NAME and apply the filter
        currentFilter = FilterType.NAME;
        filterData(filterTextField.getText());
    }

    @FXML
    void onMenuFBSubtitlesChecked(ActionEvent event) {
        // Filter video files based on the subtitles checkbox
        boolean subtitlesChecked = menuFBSubtitles.isSelected();

        GroupedTable.checkGroupForSubtitles(subtitlesChecked, tableView, dataList);
    }

    @FXML
    void onMenuGBFormatClicked(ActionEvent event) throws Exception {
        // Display a grouped table based on format
        showGroupedTable(GroupingType.FORMAT);
    }

    @FXML
    void onMenuGBAudioCodecClicked(ActionEvent event) throws Exception {
        // Display a grouped table based on audio codec
        showGroupedTable(GroupingType.AUDIO_CODEC);
    }

    @FXML
    void onMenuGBVideoCodecClicked(ActionEvent event) throws Exception {
        // Display a grouped table based on video codec
        showGroupedTable(GroupingType.VIDEO_CODEC);
    }

    @FXML
    void onMenuGBPlayerClicked(ActionEvent event) throws Exception {
        // Display a grouped table based on player
        showGroupedTable(GroupingType.PLAYER);
    }

    @FXML
    void onMenuGBLongestVideosClicked(ActionEvent event) throws Exception {
        // Display a grouped table showing the longest videos
        showGroupedTable(GroupingType.LONGEST_VIDEOS);
    }

    @FXML
    void onMenuReadClicked(ActionEvent event) throws Exception {
        // Read video data from a file
        readFromFile();
    }

    @FXML
    void onMenuSBDurationClicked(ActionEvent event) {
        // Sort video files based on duration in ascending order
        ObservableList<VideoFile> videos;
        try {
            videos = tableView.getItems();
            if (videos.isEmpty()) {
                showEmptyTableAlert();
                throw new EmptyTableException();
            }
        } catch (EmptyTableException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ObservableList<VideoFile> sortedVideos = shellSort(videos, Comparator.comparing(VideoFile::getFileDuration));
        tableView.setItems(sortedVideos);
    }

    @FXML
    void onMenuSBSizeClicked(ActionEvent event) {
        // Sort video files based on size in ascending order
        ObservableList<VideoFile> videos;
        try {
            videos = tableView.getItems();
            if (videos.isEmpty()) {
                showEmptyTableAlert();
                throw new EmptyTableException();
            }
        } catch (EmptyTableException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ObservableList<VideoFile> sortedVideos = shellSort(videos, Comparator.comparing(VideoFile::getVideoSize));
        tableView.setItems(sortedVideos);
    }

    @FXML
    void onMenuSBFormatClicked(ActionEvent event) {
        // Sort video files based on format in ascending order
        ObservableList<VideoFile> videos;
        try {
            videos = tableView.getItems();
            if (videos.isEmpty()) {
                showEmptyTableAlert();
                throw new EmptyTableException();
            }
        } catch (EmptyTableException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ObservableList<VideoFile> sortedVideos = shellSort(videos, Comparator.comparing(VideoFile::getFileFormat));
        tableView.setItems(sortedVideos);
    }

    @FXML
    void onMenuSaveClicked(ActionEvent event) throws Exception {
        // Write video data to a file
        writeToFile();
    }

    @FXML
    void onMenuVideosAddClicked(ActionEvent event) {
        // Add a video file to the table
        addVideo();
    }

    @FXML
    void onMenuVideosRemoveClicked(ActionEvent event) throws Exception {
        // Remove selected video files from the table
        deleteVideo();
    }

    //------------------------------------------------------------------------------------------

    /**
     * Displays a confirmation alert and opens the SaveFileDialog if the user agrees.
     *
     * @param alert The confirmation alert.
     */
    private void callSaveDialog(Alert alert) {
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // If OK is clicked, open the SaveFileDialog to save the data
                SaveFileDialog saveDialog = new SaveFileDialog(tableView);
                try {
                    saveDialog.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Displays an information alert with the specified header and content.
     *
     * @param header  The header text for the information alert.
     * @param content The content text for the information alert.
     */
    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Set the alert to have only the OK button
        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        // Show the alert and wait for user interaction
        alert.showAndWait();
    }

    // Implementation of shell sort algorithm
    static <T> ObservableList<T> shellSort(ObservableList<T> list, Comparator<T> comparing) {
        List<T> sortedList = new ArrayList<>(list);
        for (int d = sortedList.size() / 2; d >= 1; d /= 2) {
            for (int i = d; i < sortedList.size(); ++i) {
                T key = sortedList.get(i);
                int j;
                for (j = i; j >= d && comparing.compare(sortedList.get(j - d), key) > 0; j -= d) {
                    sortedList.set(j, sortedList.get(j - d));
                }
                sortedList.set(j, key);
            }
        }

        ObservableList<T> observableSortedList = FXCollections.observableArrayList(sortedList);
        return observableSortedList;
    }

    /**
     * Reads video data from a file and updates the table.
     *
     * <p>
     * If the table is not empty, a warning dialog is displayed to confirm the operation,
     * as reading from a file might corrupt existing data. If the user proceeds, a {@link ReadFileDialog}
     * is launched to handle the reading process.
     * </p>
     *
     * @throws Exception If an exception occurs during the file reading process.
     */
    void readFromFile() throws Exception {
        // Read video data from a file, providing a warning if the table is not empty
        if (!tableView.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Reading from file might corrupt existing data!");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ReadFileDialog readDialog = new ReadFileDialog(tableView);
                    try {
                        readDialog.start(new Stage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            ReadFileDialog readDialog = new ReadFileDialog(tableView);
            readDialog.start(new Stage());
        }
    }

    /**
     * Writes video data to a file.
     *
     * <p>
     * If the table is empty, a warning dialog is displayed to inform the user that they
     * are attempting to write an empty table to a file. If the user proceeds, a {@link SaveFileDialog}
     * is launched to handle the writing process.
     * </p>
     *
     * @throws IOException If an I/O exception occurs during the file writing process.
     */
    void writeToFile() throws Exception {
        // Write video data to a file, providing a warning if the table is empty
        if (tableView.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("You are going to write an empty table to a file!");

            callSaveDialog(alert);
        } else {
            SaveFileDialog saveDialog = new SaveFileDialog(tableView);
            saveDialog.start(new Stage());
        }
    }

    /**
     * Opens the documentation using the default system application.
     */
    void openDocumentation() {
        try {
            File fileToOpen = new File(DOCUMENTATION_PATH);

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                if (fileToOpen.exists() && fileToOpen.isFile()) {
                    desktop.open(fileToOpen);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a video by showing the add video dialog.
     *
     * @throws RuntimeException If an IOException occurs during the execution.
     */
    void addVideo() {
        try {
            showAddDialog();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a video, handling exceptions.
     *
     * @throws Exception If an exception occurs during the execution.
     */
    void deleteVideo() throws Exception {
        try {
            if (tableView.getItems().isEmpty()) {
                showEmptyTableAlert();
                throw new EmptyTableException();
            }
        } catch (EmptyTableException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (selectedFile != null) {
            tableView.getItems().remove(selectedFile);
            tableView.getSelectionModel().clearSelection();
            selectedFile = null;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oooh..");
            alert.setContentText("It seems that no item is selected.");
            alert.showAndWait();
        }
    }

    //------------------------------------------------------------------------------------------

    /**
     * Initializes the controller.
     *
     * <p>
     * This method is called automatically by JavaFX after the root element of the FXML file
     * has been completely processed. It sets up various components, including images and tooltips
     * for buttons, cell value factories for table columns, event handlers for TableView clicks,
     * a context menu for table rows, and styles for table columns.
     * </p>
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImagesAndTooltips();
        tableView.setEditable(true);
        setCellValueFactories();
        setTableViewEventHandlers();
        setRowContextMenu();
        setColumnStylesAndFactories();
        initializeDataListAndFilterListener();
        setSidebarTransition();
    }

    /**
     * Sets up the sidebar transition animation.
     */
    private void setSidebarTransition() {
        sidebar.setTranslateX(0);
        sidebarTransition = new TranslateTransition(Duration.seconds(0.3), sidebar);
        sidebarTransition.setDuration(Duration.seconds(0.5));
        sidebarTransition.setInterpolator(Interpolator.EASE_BOTH);
    }

    /**
     * Sets images and tooltips for buttons.
     */
    private void setImagesAndTooltips() {
        setButtonImages();
        setButtonTooltips();
    }

    /**
     * Sets images for buttons.
     */
    private void setButtonImages() {
        buttonAdd.setImage(new Image(IMAGE_ADD_FILEPATH));
        buttonDelete.setImage(new Image(IMAGE_DELETE_FILEPATH));
        buttonRead.setImage(new Image(IMAGE_READ_FILEPATH));
        buttonWrite.setImage(new Image(IMAGE_WRITE_FILEPATH));
        buttonHelp.setImage(new Image(IMAGE_HELP_FILEPATH));
        buttonExit.setImage(new Image(IMAGE_EXIT_FILEPATH));
    }

    /**
     * Sets tooltips for buttons.
     */
    private void setButtonTooltips() {
        Tooltip.install(buttonAdd, new Tooltip("Manually add a videofile to the table."));
        Tooltip.install(buttonDelete, new Tooltip("Delete selected videofile."));
        Tooltip.install(buttonRead, new Tooltip("Read videofiles data from .txt file."));
        Tooltip.install(buttonWrite, new Tooltip("Save your videofiles data to .txt file"));
        Tooltip.install(buttonHelp, new Tooltip("Get an instruction for the app."));
        Tooltip.install(buttonExit, new Tooltip("Close the application."));
    }

    /**
     * Sets cell value factories for each TableColumn.
     */
    private void setCellValueFactories() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("fileLocation"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("fileFormat"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("fileDuration"));
        vcodecColumn.setCellValueFactory(new PropertyValueFactory<>("videoCodec"));
        acodecColumn.setCellValueFactory(new PropertyValueFactory<>("audioCodec"));
        subtitlesColumn.setCellValueFactory(new PropertyValueFactory<>("hasSubtitles"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("videoSize"));
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        subtitlesColumn.setCellValueFactory(cellData -> cellData.getValue().hasSubtitlesProperty());
    }

    /**
     * Sets event handlers for TableView clicks.
     */
    private void setTableViewEventHandlers() {
        setSingleClickEventHandler();
        setDoubleClickEventHandler();
    }

    /**
     * Sets single click event handler for TableView.
     */
    private void setSingleClickEventHandler() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                selectedFile = tableView.getSelectionModel().getSelectedItem();
            }
        });
    }

    /**
     * Sets double click event handler for TableView.
     */
    private void setDoubleClickEventHandler() {
        tableView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                int clickedIndex = tableView.getSelectionModel().getSelectedIndex();
                if (clickedIndex == -1) {
                    try {
                        showAddDialog();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    /**
     * Sets up the context menu for TableView rows.
     * <p>
     * Creates context menu items for 'Add' and 'Delete', sets corresponding actions,
     * and associates them with a context menu. The context menu is then displayed on
     * right-click for each TableView row.
     * </p>
     */
    private void setRowContextMenu() {
        MenuItem addItem = new MenuItem("Add");
        MenuItem deleteItem = new MenuItem("Delete");

        addItem.setOnAction(event -> {
            try {
                showAddDialog();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        deleteItem.setOnAction(event -> {
            VideoFile selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                tableView.getItems().remove(selectedItem);
            }
        });

        ContextMenu rowContextMenu = new ContextMenu();
        rowContextMenu.getItems().addAll(addItem, deleteItem);

        setRowFactoryForContextMenu(rowContextMenu);
    }

    /**
     * Sets the row factory for TableView to show the context menu on right-click.
     * <p>
     * Associates the given context menu with the row factory to show the context menu
     * on right-click for each TableView row.
     * </p>
     *
     * @param rowContextMenu The context menu to be shown on right-click.
     */
    private void setRowFactoryForContextMenu(ContextMenu rowContextMenu) {
        tableView.setRowFactory(tv -> {
            TableRow<VideoFile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    rowContextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    /**
     * Sets column styles and cell factories.
     * <p>
     * Sets alignment styles for specific columns and cell factories for editable columns.
     * </p>
     */
    private void setColumnStylesAndFactories() {
        formatColumn.getStyleClass().add("center-aligned-column");
        durationColumn.getStyleClass().add("center-aligned-column");
        subtitlesColumn.getStyleClass().add("center-aligned-column");
        sizeColumn.getStyleClass().add("center-aligned-column");
        vcodecColumn.getStyleClass().add("center-aligned-column");
        acodecColumn.getStyleClass().add("center-aligned-column");
        playerColumn.getStyleClass().add("center-aligned-column");

        setCellFactoriesForColumns();
    }

    /**
     * Sets cell factories for specific columns.
     * <p>
     * Sets cell factories for editable columns, including text fields for string columns,
     * text fields with a custom converter for numeric columns, and checkboxes for boolean columns.
     * </p>
     */
    private void setCellFactoriesForColumns() {
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        pathColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        formatColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        durationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleConverter()));
        vcodecColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        acodecColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sizeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleConverter()));
        playerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        subtitlesColumn.setCellFactory(CheckBoxTableCell.forTableColumn(subtitlesColumn));
    }

    /**
     * Initializes the data list and adds a listener for the filter text field.
     * <p>
     * Initializes the data list from the TableView and adds a listener for the filter text field.
     * The listener updates the displayed data based on the filter text.
     * </p>
     */
    private void initializeDataListAndFilterListener() {
        dataList = tableView.getItems();
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
    }

    //------------------------------------------------------------------------------------------

    /**
     * Filters the data in the TableView based on the provided filter.
     *
     * @param filter The filter string to be applied.
     */
    private void filterData(String filter) {
        if (filter == null || filter.isEmpty()) {
            tableView.setItems(dataList);
        } else {
            ObservableList<VideoFile> filteredData = FXCollections.observableArrayList();
            for (VideoFile item : dataList) {
                switch (currentFilter) {
                    case NAME -> {
                        if (item.getName().toLowerCase().contains(filter.toLowerCase())) {
                            filteredData.add(item);
                        }
                    }
                    case PATH -> {
                        if (item.getFileLocation().toLowerCase().contains(filter.toLowerCase())) {
                            filteredData.add(item);
                        }
                    }
                    case FORMAT -> {
                        if (item.getFileFormat().toLowerCase().contains(filter.toLowerCase())) {
                            filteredData.add(item);
                        }
                    }
                    case DURATION -> {
                        if (item.getFileDuration().toString().contains(filter.toLowerCase())) {
                            filteredData.add(item);
                        }
                    }
                }
            }
            tableView.setItems(filteredData);
        }
    }

    /**
     * Adds a video file to the TableView.
     *
     * @param videoFile The VideoFile object to be added.
     */
    public void addVideoFile(VideoFile videoFile) {
        tableView.getItems().add(videoFile);
    }

    /**
     * Displays the add video dialog.
     *
     * @throws IOException If an error occurs while loading the dialog FXML.
     */
    public void showAddDialog() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-dialog.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Adding a video..");
        stage.getIcons().add(new Image(ICON_FILEPATH));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        AddDialogController dialogController = fxmlLoader.getController();
        dialogController.setCourseworkController(this);

        Stage mainStage = (Stage) tableView.getScene().getWindow();
        mainStage.setOnCloseRequest(event -> {
            dialogController.handleCloseRequest();
        });
    }

    /**
     * Displays a grouped table based on the specified grouping type.
     *
     * @param groupingType The type of grouping to apply.
     * @throws Exception If an error occurs while creating or displaying the grouped table.
     */
    public void showGroupedTable(GroupingType groupingType) throws Exception {
        ObservableList<VideoFile> videoFiles;
        try {
            videoFiles = tableView.getItems();
            if (videoFiles.isEmpty()) {
                showEmptyTableAlert();
                throw new EmptyTableException();
            }
        } catch (EmptyTableException e) {
            System.out.println(e.getMessage());
            return;
        }
        GroupedTable groupedTable = new GroupedTable(videoFiles, groupingType);
        groupedTable.start(new Stage());

        Stage mainStage = (Stage) tableView.getScene().getWindow();
        mainStage.setOnCloseRequest(event -> {
            groupedTable.handleCloseRequest();
        });
    }

    /**
     * Handles the commit event when a cell is edited in the TableView.
     *
     * @param event The CellEditEvent containing information about the edit.
     * @throws Exception If an error occurs during the handling of the edit commit.
     */
    public void onEditCommit(TableColumn.CellEditEvent<VideoFile, Object> event) throws Exception {
        // Get necessary information from the event
        TableView<VideoFile> tableView = event.getTableView();
        VideoFile file = tableView.getSelectionModel().getSelectedItem();
        TableColumn<VideoFile, Object> column = event.getTableColumn();
        Object newValue = event.getNewValue();

        // Check if the file, column, and new value are not null
        if (file != null && column != null) {
            Double newDoubleValue;

            // Identify the column by its ID and update the corresponding property in the VideoFile
            String columnName = column.getId();
            switch (columnName) {
                case "nameColumn":
                    file.setName((String) newValue);
                    break;
                case "pathColumn":
                    file.setFileLocation((String) newValue);
                    break;
                case "formatColumn":
                    file.setFileFormat((String) newValue);
                    break;
                case "durationColumn":
                    try {
                        // Check if the new value is a Double, otherwise throw an exception
                        if (newValue instanceof Double) {
                            newDoubleValue = (Double) newValue;
                        } else {
                            throw new InvalidDataException();
                        }
                    } catch (InvalidDataException e) {
                        // Handle the exception by printing the message and showing an alert
                        System.out.println(e.getMessage());
                        showInvalidDataAlert();
                        return;
                    }
                    file.setFileDuration(newDoubleValue);
                    break;
                case "vcodecColumn":
                    file.setVideoCodec((String) newValue);
                    break;
                case "acodecColumn":
                    file.setAudioCodec((String) newValue);
                    break;
                case "subtitlesColumn":
                    // Update the 'hasSubtitles' property based on the new value
                    file.setHasSubtitles(newValue.equals("Yes"));
                    break;
                case "sizeColumn":
                    try {
                        // Check if the new value is a Double, otherwise throw an exception
                        if (newValue instanceof Double) {
                            newDoubleValue = (Double) newValue;
                        } else {
                            throw new InvalidDataException();
                        }
                    } catch (InvalidDataException e) {
                        // Handle the exception by printing the message and showing an alert
                        System.out.println(e.getMessage());
                        showInvalidDataAlert();
                        return;
                    }
                    file.setVideoSize(newDoubleValue);
                    break;
                case "playerColumn":
                    file.setPlayer((String) newValue);
                    break;
            }
        }

        // Clear the selection and reset the selected file
        tableView.getSelectionModel().clearSelection();
        selectedFile = null;
    }
}