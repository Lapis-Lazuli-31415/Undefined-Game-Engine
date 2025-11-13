module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;


    opens app to javafx.fxml;
    exports app;
}