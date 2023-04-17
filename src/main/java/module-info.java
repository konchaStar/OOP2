module com.oop.oop2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;
    requires gson.extras;

    opens by.oop.oop2 to javafx.fxml;
    exports by.oop.oop2;
    exports fabrics;
    opens fabrics to javafx.fxml;
}