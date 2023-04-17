package GUI;

import by.oop.oop2.Controller;
import fabrics.Factory;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import transport.Transport;

import java.util.Optional;

public class Windows {
    private static HBox addBox = new HBox();
    private static HBox changeBox = new HBox();
    public static void showAddWindow(){
        Stage stage = new Stage();
        VBox container = new VBox();
        ChoiceBox<String> type = new ChoiceBox<>();
        HBox fields = new HBox();
        for(String availableClass : Controller.getFactories().keySet()){
            type.getItems().add(availableClass);
        }
        TextField name = new TextField();
        type.setOnAction(x -> {
            addBox.getChildren().clear();
            addBox.getChildren().add(new VBox(new Label("Название")));
            addBox.getChildren().add(new VBox(name));
            Factory factory = Controller.getFactories().get(type.getValue());
            factory.getFields(addBox, Optional.empty());
        });
        type.setValue(type.getItems().get(0));
        container.getChildren().add(type);
        container.getChildren().add(addBox);
        Button addButton = new Button("Добавить");
        Button cancelButton = new Button("Отменить");
        container.getChildren().add(new HBox(addButton, cancelButton));
        cancelButton.setOnMousePressed(x -> {
            stage.close();
        });
        addButton.setOnMousePressed(x -> {
            Factory factory = Controller.getFactories().get(type.getValue());
            if(name.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Check all fields!");
                alert.showAndWait();
            } else if(Controller.addTransport(factory.getTransport(addBox), name.getText())) {
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Check all fields!");
                alert.showAndWait();
            }
        });

        stage.setScene(new Scene(container));
        stage.setResizable(true);
        stage.show();
    }
    public static void showEditWindow(String type, String name, Optional<? extends Transport> transport, int index) {
        Stage stage = new Stage();
        VBox container = new VBox();
        HBox fields = new HBox();
        Factory factory = Controller.getFactories().get(type);
        changeBox.getChildren().clear();
        TextField nameField = new TextField(name);
        changeBox.getChildren().add(new VBox(new Label("Название")));
        changeBox.getChildren().add(new VBox(nameField));
        factory.getFields(changeBox, transport);
        container.getChildren().add(changeBox);
        Button addButton = new Button("Сохранить");
        Button cancelButton = new Button("Отменить");
        container.getChildren().add(new HBox(addButton, cancelButton));
        cancelButton.setOnMousePressed(x -> {
            stage.close();
        });
        addButton.setOnMousePressed(x -> {
            Factory saveFactory = Controller.getFactories().get(type);

            if(!nameField.getText().equals("") && Controller.changeTransport(factory.getTransport(changeBox), nameField.getText(), index)) {
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Check all fields!");
                alert.showAndWait();
            }
        });
        stage.setScene(new Scene(container));
        stage.setResizable(true);
        stage.show();
    }

}