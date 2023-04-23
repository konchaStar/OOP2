module com.oop.oop2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;
    requires gson.extras;
    requires org.apache.commons.codec;

    opens by.oop.oop2 to javafx.fxml, com.google.gson;
    exports by.oop.oop2;
    exports fabrics;

    opens transport to com.google.gson;
    opens fabrics to javafx.fxml;
}