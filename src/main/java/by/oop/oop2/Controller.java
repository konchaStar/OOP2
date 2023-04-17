package by.oop.oop2;

import GUI.Name;
import GUI.Windows;
import fabrics.Factory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import serializer.BinarySerializer;
import serializer.JsonSerializer;
import serializer.Serializer;
import transport.*;
import fabrics.*;

import java.io.File;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable {
    private static ObservableList<String> names;
    private static ObservableList<String> types;
    private static ArrayList<Pair<String, Transport>> transport = new ArrayList<>();
    private static HashMap<String, Factory> factories = new HashMap<>();
    private static HashMap<String, Serializer> serializers = new HashMap<>();

    @FXML
    private ListView<String> objectsList;
    @FXML
    private Button btnAdd;
    @FXML
    private ListView<String> objectsTypeList;
    public static boolean addTransport(Optional<? extends Transport> transport, String name){
        if(transport.isEmpty()){
            return false;
        }
        Controller.transport.add(new Pair(name, transport.get()));
        names.add(name);
        types.add(transport.get().getClass().getAnnotation(Name.class).value());
        return true;
    }
    public static boolean changeTransport(Optional<Transport> transport, String name, int index) {
        if(transport.isEmpty()){
            return false;
        }
        Controller.transport.set(index, new Pair<>(name, transport.get()));
        names.remove(index);
        names.add(index, name);
        return true;
    }
    private static void initFabrics(){
        factories.put(Car.class.getAnnotation(Name.class).value(), new CarFactory());
        factories.put(Bus.class.getAnnotation(Name.class).value(), new BusFactory());
        factories.put(Truck.class.getAnnotation(Name.class).value(), new TruckFactory());
        factories.put(Bicycle.class.getAnnotation(Name.class).value(), new BicycleFactory());
        factories.put(MotorizedBoat.class.getAnnotation(Name.class).value(), new BoatFactory());
    }
    public static HashMap<String, Factory> getFactories() {
        return factories;
    }
    @FXML
    private void onAddClick() {
        Windows.showAddWindow();
    }
    @FXML
    private void onChangeClick() {
        if(objectsList.getSelectionModel().getSelectedIndex() != -1){

            String name = objectsList.getItems().get(objectsList.getSelectionModel().getSelectedIndex()).toString();
            Optional<Transport> transport = Optional.of(Controller.transport.get(objectsList.getSelectionModel().getSelectedIndex()).getValue());
            String type = transport.get().getClass().getAnnotation(Name.class).value();
            Windows.showEditWindow(type, name, transport, objectsList.getSelectionModel().getSelectedIndex());
        }
    }
    @FXML
    private void onDelete(){
        if(objectsList.getSelectionModel().getSelectedIndex() != -1){
            transport.remove(names.get(objectsList.getSelectionModel().getSelectedIndex()));
            types.remove(objectsList.getSelectionModel().getSelectedIndex());
            names.remove(objectsList.getSelectionModel().getSelectedIndex());
        }
    }
    @FXML
    private void onSave() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Binary files (*.bin)", "*.bin"));
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json files (*.json)", "*.json"));
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
            File file = chooser.showOpenDialog(Application.stStage);
            Serializer serializer = serializers.get(file.getName().substring(file.getName().lastIndexOf(".")));

            serializer.serialize(transport, file);
        } catch (Exception e) {
            System.out.println("file not found");
        }
    }
    @FXML
    private void onOpen() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Binary files (*.bin)","*.bin"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json files (*.json)","*.json"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)","*.txt"));
        File file = chooser.showOpenDialog(Application.stStage);
        Serializer serializer = serializers.get(file.getName().substring(file.getName().lastIndexOf(".")));
        ArrayList<Pair<String, Transport>> transports = serializer.deserialize(file);
        for(Pair<String, Transport> transport : transports) {
            addTransport(Optional.of(transport.getValue()), transport.getKey());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initFabrics();
        names = objectsList.getItems();
        types = objectsTypeList.getItems();
        serializers.put(".bin", new BinarySerializer());
        serializers.put(".json", new JsonSerializer());
//        addTransport(Optional.of(new Car("bmw", "red", new Engine(20000, 6), "1234", Car.CarTypes.SPORTCAR, 5, 4)), "Моя машина");
//        addTransport(Optional.of(new Bicycle("stels", "blue", 24, 2, 1)), "Мой байк");
//        addTransport(Optional.of(new Bus("mercedes", "yellow", Bus.BusTypes.SCHOOL, new Engine(10000, 6), "5647", 20, 4)), "Школьный автобус");
    }
}