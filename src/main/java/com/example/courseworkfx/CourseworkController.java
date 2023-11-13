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
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.example.courseworkfx.dialogs.EmptyTableAlert.showEmptyTableAlert;
import static com.example.courseworkfx.dialogs.InvalidDataAlert.showInvalidDataAlert;

/**
 * Controller class for the main application window.
 */
public class CourseworkController implements Initializable, Constants {

    // Enumeration for filtering options
    public enum FilterType {
        NAME,
        DURATION,
        FORMAT,
        PATH,
    }

    // Enumeration for grouping options
    public enum GroupingType {
        AUDIO_CODEC,
        VIDEO_CODEC,
        PLAYER,
        LONGEST_VIDEOS
    }

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
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private MenuItem menuFBPath;

    @FXML
    private MenuItem menuFBName;

    @FXML
    private CheckMenuItem menuFBSubtitles;

    @FXML
    private MenuItem menuGBAudioCodec;

    @FXML
    private MenuItem menuGBVideoCodec;

    @FXML
    private MenuItem menuGBPlayer;

    @FXML
    private MenuItem menuGBLongestVideos;

    @FXML
    private MenuItem menuRead;

    @FXML
    private MenuItem menuSBDuration;

    @FXML
    private MenuItem menuSBSize;

    @FXML
    private MenuItem menuSBFormat;

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
    private TableColumn<VideoFile, String> formatColumn;

    @FXML
    private TableColumn<VideoFile, Double> durationColumn;

    @FXML
    private TableColumn<VideoFile, String> vcodecColumn;

    @FXML
    private TableColumn<VideoFile, String> acodecColumn;

    @FXML
    private TableColumn<VideoFile, Boolean> subtitlesColumn;

    @FXML
    private TableColumn<VideoFile, Double> sizeColumn;

    @FXML
    private TableColumn<VideoFile, String> playerColumn;

    // Other class fields
    private ObservableList<VideoFile> dataList;

    private FilterType currentFilter = FilterType.NAME;

    private VideoFile selectedFile;

    // Methods annotated with @FXML handle various UI events

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

    private void callSaveDialog(Alert alert) {
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // If the user clicks OK, open the SaveFileDialog to save the data
                SaveFileDialog saveDialog = new SaveFileDialog(tableView);
                try {
                    saveDialog.start(new Stage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
    void onImageWriteClicked(MouseEvent event) throws IOException {
        // Handle the event when the "Write" image is clicked
        writeToFile();
    }

    @FXML
    void onMenuCalcAvgDurationClicked(ActionEvent event) throws Exception {
        // Calculate and display the average duration of video files
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Average video file duration.");
        alert.setContentText("The calculated result is: " + String.format(Locale.US, "%.3f", result) + " min(s)");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuCalcAvgSizeClicked(ActionEvent event) throws Exception {
        // Calculate and display the average size of video files
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Average video file size.");
        alert.setContentText("The calculated result is: " + String.format(Locale.US, "%.3f", result) + " MB");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuCalcMaxDurationClicked(ActionEvent event) throws Exception {
        // Calculate and display the maximum duration of video files
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Maximum video file duration.");
        alert.setContentText("The calculated result is: " + result + " min(s)");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuCalcMaxSizeClicked(ActionEvent event) throws Exception {
        // Calculate and display the maximum size of video files
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Maximum video file size.");
        alert.setContentText("The calculated result is: " + result + " MB");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuCalcMinSizeClicked(ActionEvent event) throws Exception {
        // Calculate and display the minimum size of video files
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Minimum video file size.");
        alert.setContentText("The calculated result is: " + result + " MB");

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
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

        ObservableList<VideoFile> filteredList = FXCollections.observableArrayList();

        if (subtitlesChecked) {
            for (VideoFile videoFile : dataList) {
                if (videoFile.ifHasSubtitles()) {
                    filteredList.add(videoFile);
                }
            }
        } else {
            filteredList.addAll(dataList);
        }

        tableView.setItems(filteredList);
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
        shellSort(videos, Comparator.comparing(VideoFile::getFileDuration));
        tableView.setItems(videos);
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
        shellSort(videos, Comparator.comparing(VideoFile::getVideoSize));
        tableView.setItems(videos);
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
        shellSort(videos, Comparator.comparing(VideoFile::getFileFormat));
        tableView.setItems(videos);
    }

    static <T> void shellSort(ObservableList<T> list, Comparator<T> comparing) {
        // Implementation of shell sort algorithm
        for (int d = list.size() / 2; d >= 1; d /= 2) {
            for (int i = d; i < list.size(); ++i) {
                T key = list.get(i);
                int j;
                for (j = i; j >= d && comparing.compare(list.get(j - d), key) > 0; j -= d) {
                    list.set(j, list.get(j - d));
                }
                list.set(j, key);
            }
        }
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

    void writeToFile() throws IOException {
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

    // Method to open documentation using the default system application
    void openDocumentation() {
        try {
            // Create a File object representing the documentation file
            File fileToOpen = new File(DOCUMENTATION_PATH);

            // Check if the Desktop API is supported (common on desktop environments)
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                // Check if the file exists and is a regular file
                if (fileToOpen.exists() && fileToOpen.isFile()) {
                    // Open the file with the default system application
                    desktop.open(fileToOpen);
                }
            }
        } catch (IOException e) {
            // Print the stack trace if an exception occurs during file opening
            System.out.println(e.getMessage());
        }
    }

    // Method to add a video by showing the add video dialog
    void addVideo() {
        try {
            // Show the add video dialog
            showAddDialog();
        } catch (IOException e) {
            // Wrap the IOException in a RuntimeException and throw it
            throw new RuntimeException(e);
        }
    }

    // Method to delete a video, handling exceptions
    void deleteVideo() throws Exception {
        try {
            // Check if the table is empty
            if (tableView.getItems().isEmpty()) {
                // Show an alert for an empty table and throw an EmptyTableException
                showEmptyTableAlert();
                throw new EmptyTableException();
            }
        } catch (EmptyTableException e) {
            // Print the exception message and return from the method
            System.out.println(e.getMessage());
            return;
        }

        // Check if a video is selected for deletion
        if (selectedFile != null) {
            // Remove the selected video from the table
            tableView.getItems().remove(selectedFile);
            tableView.getSelectionModel().clearSelection();
            selectedFile = null;
        } else {
            // If no video is selected, show an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oooh..");
            alert.setContentText("It seems that no item is selected.");
            alert.showAndWait();
        }
    }

    // Method to initialize the controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set images for buttons and tooltips
        setImagesAndTooltips();

        // Make the TableView editable
        tableView.setEditable(true);

        // Set cell value factories for each TableColumn
        setCellValueFactories();

        // Set event handlers for TableView clicks
        setTableViewEventHandlers();

        // Set up context menu for TableView rows
        setRowContextMenu();

        // Set column styles and cell factories
        setColumnStylesAndFactories();

        // Set up data list and add listener for the filter text field
        initializeDataListAndFilterListener();

        sidebar.setTranslateX(0);
        sidebarTransition = new TranslateTransition(Duration.seconds(0.3), sidebar);
        sidebarTransition.setDuration(Duration.seconds(0.5));
        sidebarTransition.setInterpolator(Interpolator.EASE_BOTH);
    }

    // Method to set images for buttons and tooltips
    private void setImagesAndTooltips() {
        // Set images for buttons
        setButtonImages();

        // Set tooltips for buttons
        setButtonTooltips();
    }

    // Method to set images for buttons
    private void setButtonImages() {
        // Load images and set them to corresponding buttons
        buttonAdd.setImage(new Image(IMAGE_ADD_FILEPATH));
        buttonDelete.setImage(new Image(IMAGE_DELETE_FILEPATH));
        buttonRead.setImage(new Image(IMAGE_READ_FILEPATH));
        buttonWrite.setImage(new Image(IMAGE_WRITE_FILEPATH));
        buttonHelp.setImage(new Image(IMAGE_HELP_FILEPATH));
        buttonExit.setImage(new Image(IMAGE_EXIT_FILEPATH));
    }

    // Method to set tooltips for buttons
    private void setButtonTooltips() {
        // Create tooltips and install them for each button
        Tooltip.install(buttonAdd, new Tooltip("Manually add a videofile to the table."));
        Tooltip.install(buttonDelete, new Tooltip("Delete selected videofile."));
        Tooltip.install(buttonRead, new Tooltip("Read videofiles data from .txt file."));
        Tooltip.install(buttonWrite, new Tooltip("Save your videofiles data to .txt file"));
        Tooltip.install(buttonHelp, new Tooltip("Get an instruction for the app."));
        Tooltip.install(buttonExit, new Tooltip("Close the application."));
    }

    // Method to set cell value factories for each TableColumn
    private void setCellValueFactories() {
        // Set PropertyValueFactory for each TableColumn
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

    // Method to set event handlers for TableView clicks
    private void setTableViewEventHandlers() {
        // Set click event handlers for single and double clicks on TableView
        setSingleClickEventHandler();
        setDoubleClickEventHandler();
    }

    // Method to set single click event handler for TableView
    private void setSingleClickEventHandler() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                // Handle single click on TableView
                selectedFile = tableView.getSelectionModel().getSelectedItem();
            }
        });
    }

    // Method to set double click event handler for TableView
    private void setDoubleClickEventHandler() {
        tableView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                int clickedIndex = tableView.getSelectionModel().getSelectedIndex();
                if (clickedIndex == -1) {
                    try {
                        // Handle double click on TableView when no item is selected
                        showAddDialog();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    // Method to set up context menu for TableView rows
    private void setRowContextMenu() {
        // Create context menu items
        MenuItem addItem = new MenuItem("Add");
        MenuItem deleteItem = new MenuItem("Delete");

        // Set actions for context menu items
        addItem.setOnAction(event -> {
            try {
                // Handle 'Add' context menu item
                showAddDialog();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        deleteItem.setOnAction(event -> {
            // Handle 'Delete' context menu item
            VideoFile selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                tableView.getItems().remove(selectedItem);
            }
        });

        // Create context menu and add items
        ContextMenu rowContextMenu = new ContextMenu();
        rowContextMenu.getItems().addAll(addItem, deleteItem);

        // Set row factory to show context menu on right-click
        setRowFactoryForContextMenu(rowContextMenu);
    }

    // Method to set row factory for TableView to show context menu on right-click
    private void setRowFactoryForContextMenu(ContextMenu rowContextMenu) {
        tableView.setRowFactory(tv -> {
            TableRow<VideoFile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    // Show context menu on right-click
                    rowContextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    // Method to set column styles and cell factories
    private void setColumnStylesAndFactories() {
        // Set alignment styles for specific columns
        formatColumn.setStyle("-fx-alignment: CENTER;");
        durationColumn.setStyle("-fx-alignment: CENTER;");
        subtitlesColumn.setStyle("-fx-alignment: CENTER;");
        sizeColumn.setStyle("-fx-alignment: CENTER;");
        vcodecColumn.setStyle("-fx-alignment: CENTER;");
        acodecColumn.setStyle("-fx-alignment: CENTER;");
        playerColumn.setStyle("-fx-alignment: CENTER;");

        // Set cell factories for specific columns
        setCellFactoriesForColumns();
    }

    // Method to set cell factories for specific columns
    private void setCellFactoriesForColumns() {
        // Set cell factories for editable columns
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

    // Method to initialize data list and add listener for the filter text field
    private void initializeDataListAndFilterListener() {
        // Initialize data list from TableView
        dataList = tableView.getItems();

        // Add listener for the filter text field
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
    }

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
    }

    @FXML
    private void toggleSidebar() {
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