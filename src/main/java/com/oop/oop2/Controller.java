package com.oop.oop2;

import GUI.Name;
import GUI.Windows;
import fabrics.Factory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import transport.*;
import fabrics.*;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    private static ObservableList<String> names;
    private static ObservableList<String> types;
    private static HashMap<String, Transport> transport = new HashMap<>();
    private static HashMap<String, Factory> factories = new HashMap<>();
    private static List<Class<? extends Transport>> availableClasses= new ArrayList<>();
    @FXML
    private ListView<String> objectsList;
    @FXML
    private Button btnAdd;
    @FXML
    private ListView<String> objectsTypeList;
    public static boolean addTransport(Optional<? extends Transport> transport, String name, String type){
        if(transport.isEmpty()){
            return false;
        }
        Controller.transport.put(name, transport.get());
        names.add(name);
        types.add(type);
        return true;
    }
    public static boolean changeTransport(Optional<Transport> transport, String name) {
        if(transport.isEmpty()){
            return false;
        }
        Controller.transport.replace(name, transport.get());
        return true;
    }
    private static void initFabrics(){
        factories.put(Car.class.getAnnotation(Name.class).value(), new CarFactory());
        factories.put(Bus.class.getAnnotation(Name.class).value(), new BusFactory());
        factories.put(Truck.class.getAnnotation(Name.class).value(), new TruckFactory());
        factories.put(Bicycle.class.getAnnotation(Name.class).value(), new BicycleFactory());
        factories.put(MotorizedBoat.class.getAnnotation(Name.class).value(), new BoatFactory());
    }
    public static List<Class<? extends Transport>> getAvailableClasses() {
        return availableClasses;
    }
    public static HashMap<String, Factory> getFactories() {
        return factories;
    }
    private static void initClasses(){
        availableClasses.add(Car.class);
        availableClasses.add(Bus.class);
        availableClasses.add(Bicycle.class);
        availableClasses.add(MotorizedBoat.class);
        availableClasses.add(Truck.class);
    }
    @FXML
    private void onAddClick() {
        Windows.showAddWindow();
    }
    @FXML
    private void onChangeClick() {
        if(objectsList.getSelectionModel().getSelectedIndex() != -1){
            String type = types.get(objectsList.getSelectionModel().getSelectedIndex());
            String name = objectsList.getItems().get(objectsList.getSelectionModel().getSelectedIndex()).toString();
            Windows.showEditWindow(type, name, Optional.of(transport.get(name)));
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initFabrics();
        initClasses();
        names = objectsList.getItems();
        types = objectsTypeList.getItems();
        addTransport(Optional.of(new Car("bmw", "red", new Engine(20000, 6), "1234", Car.CarTypes.SPORTCAR, 5, 4)), "Моя машина", "Автомобиль");
    }
}