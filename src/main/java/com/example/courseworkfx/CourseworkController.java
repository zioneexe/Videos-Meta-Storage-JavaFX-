package com.example.courseworkfx;

import com.example.courseworkfx.dialogs.ReadFileDialog;
import com.example.courseworkfx.dialogs.SaveFileDialog;
import com.example.courseworkfx.exceptions.EmptyTableException;
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
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.example.courseworkfx.dialogs.EmptySelectionAlert.showEmptyTableAlert;

public class CourseworkController implements Initializable, Constants {

    public enum FilterType {
        NAME,
        DURATION,
        FORMAT,
        PATH,
    }

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
    private MenuItem menuGBCodec;

    @FXML
    private MenuItem menuGBPlayer;

    @FXML
    private MenuItem menuRead;

    @FXML
    private MenuItem menuSBDuration;

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

    private ObservableList<VideoFile> dataList;

    private FilterType currentFilter = FilterType.NAME;

    private VideoFile selectedFile;

    private boolean isSidebarVisible = false;

    @FXML
    void onMenuAboutClicked(ActionEvent event) {

    }

    @FXML
    void onImageAddClicked(MouseEvent event) {
        addVideo();
    }

    @FXML
    void onImageDeleteClicked(MouseEvent event) throws Exception {
        deleteVideo();
    }

    @FXML
    void onImageExitClicked(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void onImageHelpClicked(MouseEvent event) {

    }

    @FXML
    void onImageReadClicked(MouseEvent event) throws Exception {
        readFromFile();
    }

    @FXML
    void onImageWriteClicked(MouseEvent event) throws IOException {
        writeToFile();
    }
    @FXML
    void onMenuCalcAvgDurationClicked(ActionEvent event) throws EmptyTableException {
        ObservableList<VideoFile> videos = tableView.getItems();
        if (videos.isEmpty()) throw new EmptyTableException("");
        Double result = 0.0;

        for (VideoFile file : videos) {
            result += file.getFileDuration();
        }
        result /= videos.size();


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Average video file duration.");
        alert.setContentText("The calculated result is: " + String.format(Locale.US, "%.3f", result));

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuCalcAvgSizeClicked(ActionEvent event) throws EmptyTableException {
        ObservableList<VideoFile> videos = tableView.getItems();
        if (videos.isEmpty()) throw new EmptyTableException("");
        Double result = 0.0;

        for (VideoFile file : videos) {
            result += file.getVideoSize();
        }
        result /= videos.size();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Average video file size.");
        alert.setContentText("The calculated result is: " + String.format(Locale.US, "%.3f", result));

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuCalcMaxDurationClicked(ActionEvent event) throws EmptyTableException {
        ObservableList<VideoFile> videos = tableView.getItems();
        if (videos.isEmpty()) throw new EmptyTableException("");
        Double result = videos.get(0).getFileDuration();

        for (VideoFile file : videos) {
            if (file.getFileDuration() > result) result = file.getFileDuration();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Maximum video file duration.");
        alert.setContentText("The calculated result is: " + result);

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuCalcMaxSizeClicked(ActionEvent event) throws EmptyTableException {
        ObservableList<VideoFile> videos = tableView.getItems();
        if (videos.isEmpty()) throw new EmptyTableException("");
        Double result = videos.get(0).getVideoSize();

        for (VideoFile file : videos) {
            if (file.getVideoSize() > result) result = file.getVideoSize();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Maximum video file size.");
        alert.setContentText("The calculated result is: " + result);

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuCalcMinSizeClicked(ActionEvent event) throws EmptyTableException {
        ObservableList<VideoFile> videos = tableView.getItems();
        if (videos.isEmpty()) throw new EmptyTableException("");
        Double result = videos.get(0).getVideoSize();

        for (VideoFile file : videos) {
            if (file.getVideoSize() < result) result = file.getVideoSize();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Minimum video file size.");
        alert.setContentText("The calculated result is: " + result);

        alert.getButtonTypes().setAll(javafx.scene.control.ButtonType.OK);

        alert.showAndWait();
    }

    @FXML
    void onMenuFBDurationClicked(ActionEvent event) {
        currentFilter = FilterType.DURATION;
        filterData(filterTextField.getText());
    }

    @FXML
    void onMenuFBFormatClicked(ActionEvent event) {
        currentFilter = FilterType.FORMAT;
        filterData(filterTextField.getText());
    }

    @FXML
    void onMenuFBPathClicked(ActionEvent event) {
        currentFilter = FilterType.PATH;
        filterData(filterTextField.getText());
    }

    @FXML
    void onMenuFBNameClicked(ActionEvent event) {
        currentFilter = FilterType.NAME;
        filterData(filterTextField.getText());
    }

    @FXML
    void onMenuFBSubtitlesChecked(ActionEvent event) {
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
    void onMenuGBCodecClicked(ActionEvent event) {

    }

    @FXML
    void onMenuGBPlayerClicked(ActionEvent event) {
    }

    @FXML
    void onMenuReadClicked(ActionEvent event) throws Exception {
        readFromFile();
    }

    @FXML
    void onMenuSBDurationClicked(ActionEvent event) {
        ObservableList<VideoFile> videos = tableView.getItems();
        shellSort(videos, Comparator.comparing(VideoFile::getFileDuration));
        tableView.setItems(videos);
    }

    private <T> void shellSort(ObservableList<T> list, Comparator<T> comparing) {
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
    void onMenuSBSizeClicked(ActionEvent event) {
        ObservableList<VideoFile> videos = tableView.getItems();
        shellSort(videos, Comparator.comparing(VideoFile::getVideoSize));
        tableView.setItems(videos);
    }

    @FXML
    void onMenuSaveClicked(ActionEvent event) throws Exception {
        writeToFile();
    }

    @FXML
    void onMenuVideosAddClicked(ActionEvent event) {
        addVideo();
    }

    @FXML
    void onMenuVideosRemoveClicked(ActionEvent event) throws Exception {
        deleteVideo();
    }

    void readFromFile() throws Exception {
        ReadFileDialog readDialog = new ReadFileDialog(tableView);
        readDialog.start(new Stage());
    }

    void writeToFile() throws IOException {
        SaveFileDialog saveDialog = new SaveFileDialog(tableView);
        saveDialog.start(new Stage());
    }
    void addVideo() {
        try {
            showAddDialog();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    void deleteVideo() throws Exception {
        try {
            if (selectedFile != null) {
                tableView.getItems().remove(selectedFile);
                tableView.getSelectionModel().clearSelection();
            } else {
                throw new EmptyTableException();
            }
        } catch (EmptyTableException e) {
            System.out.println(e.getMessage());
            showEmptyTableAlert();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image imageAdd = new Image(IMAGE_ADD_FILEPATH);
        buttonAdd.setImage(imageAdd);
        Image imageDelete = new Image(IMAGE_DELETE_FILEPATH);
        buttonDelete.setImage(imageDelete);
        Image imageRead = new Image(IMAGE_READ_FILEPATH);
        buttonRead.setImage(imageRead);
        Image imageWrite = new Image(IMAGE_WRITE_FILEPATH);
        buttonWrite.setImage(imageWrite);
        Image imageHelp = new Image(IMAGE_HELP_FILEPATH);
        buttonHelp.setImage(imageHelp);
        Image imageExit = new Image(IMAGE_EXIT_FILEPATH);
        buttonExit.setImage(imageExit);

        tableView.setEditable(true);

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

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                selectedFile = tableView.getSelectionModel().getSelectedItem();
            }
        });

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

        MenuItem addItem = new MenuItem("Add");
        addItem.setOnAction(event -> {
            try {
                showAddDialog();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ContextMenu rowContextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            VideoFile selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                tableView.getItems().remove(selectedItem);
            }
        });

        rowContextMenu.getItems().add(addItem);
        rowContextMenu.getItems().add(deleteItem);

        tableView.setRowFactory(tv -> {
            TableRow<VideoFile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    rowContextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });

        formatColumn.setStyle("-fx-alignment: CENTER;");
        durationColumn.setStyle("-fx-alignment: CENTER;");
        subtitlesColumn.setStyle("-fx-alignment: CENTER;");
        sizeColumn.setStyle("-fx-alignment: CENTER;");
        vcodecColumn.setStyle("-fx-alignment: CENTER;");
        acodecColumn.setStyle("-fx-alignment: CENTER;");
        playerColumn.setStyle("-fx-alignment: CENTER;");

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        pathColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        formatColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        durationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        vcodecColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        acodecColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sizeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        playerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        subtitlesColumn.setCellFactory(CheckBoxTableCell.forTableColumn(subtitlesColumn));


        dataList = tableView.getItems();
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
    }

    private void filterData(String filter) {
        if (filter == null || filter.isEmpty()) {
            tableView.setItems(dataList);
        } else {
            ObservableList<VideoFile> filteredData = FXCollections.observableArrayList();
            for (VideoFile item : dataList) {
                switch (currentFilter) {
                    case NAME -> {
                        if (item.getName().toLowerCase().contains(filter.toLowerCase())) {
                            filteredData.add(item);}
                    }
                    case PATH -> {
                        if (item.getFileLocation().toLowerCase().contains(filter.toLowerCase())) {
                            filteredData.add(item);}
                    }
                    case FORMAT -> {
                        if (item.getFileFormat().toLowerCase().contains(filter.toLowerCase())) {
                            filteredData.add(item);}
                    }
                    case DURATION -> {
                        if (item.getFileDuration().toString().contains(filter.toLowerCase())) {
                            filteredData.add(item);}
                    }
                }
            }
            tableView.setItems(filteredData);
        }
    }

    public void addVideoFile(VideoFile videoFile) {
        tableView.getItems().add(videoFile);
    }

    public void toggleSidebar(ActionEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sidebar);

        if (isSidebarVisible) {
            transition.setToX(0);
        } else {
            transition.setToX(sidebar.getWidth()-5);
        }

        transition.play();
        isSidebarVisible = !isSidebarVisible;
    }

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

    public void onEditCommit(TableColumn.CellEditEvent<VideoFile, Object> event) {
        TableView<VideoFile> tableView = event.getTableView();
        VideoFile file = tableView.getSelectionModel().getSelectedItem();
        TableColumn<VideoFile, Object> column = event.getTableColumn();
        Object newValue = event.getNewValue();

        if (file != null && column != null) {
            String columnName = column.getId();
            switch (columnName) {
                case "name":
                    file.setName((String) newValue);
                    break;
                case "fileLocation":
                    file.setFileLocation((String) newValue);
                    break;
                case "fileFormat":
                    file.setFileFormat((String) newValue);
                    break;
                case "fileDuration":
                    file.setFileDuration((Double) newValue);
                    break;
                case "videoCodec":
                    file.setVideoCodec((String) newValue);
                    break;
                case "audioCodec":
                    file.setAudioCodec((String) newValue);
                    break;
                case "hasSubtitles":
                    file.setHasSubtitles(newValue.equals("Yes"));
                    break;
                case "videoSize":
                    file.setVideoSize((Double) newValue);
                    break;
                case "player":
                    file.setPlayer((String) newValue);
                    break;
            }
        }

        tableView.getSelectionModel().clearSelection();
    }
}