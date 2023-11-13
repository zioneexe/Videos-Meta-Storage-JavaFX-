module com.example.courseworkfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.courseworkfx to javafx.fxml;
    exports com.example.courseworkfx;
    exports com.example.courseworkfx.exceptions;
    opens com.example.courseworkfx.exceptions to javafx.fxml;
}