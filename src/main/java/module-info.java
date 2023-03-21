module com.oop.oop2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.oop.oop2 to javafx.fxml;
    exports com.oop.oop2;
    exports fabrics;
    opens fabrics to javafx.fxml;
}