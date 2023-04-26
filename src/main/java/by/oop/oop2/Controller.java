package by.oop.oop2;

import GUI.Name;
import GUI.Windows;
import fabrics.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.example.*;
import serializer.*;
import transport.*;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Controller implements Initializable {
    private static ObservableList<String> names;
    private static ObservableList<String> types;
    private static ArrayList<MyPair<String, Transport>> transport = new ArrayList<>();
    private static HashMap<String, Factory> factories = new HashMap<>();
    private static HashMap<String, Serializer> serializers = new HashMap<>();
    private static HashMap<String, Plugin> plugins = new HashMap<>();
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnCancel;
    @FXML
    private ChoiceBox<String> pluginChoice;
    @FXML
    private Pane pluginPane;
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
        Controller.transport.add(new MyPair(name, transport.get()));
        names.add(name);
        types.add(transport.get().getClass().getAnnotation(Name.class).value());
        return true;
    }
    public static boolean changeTransport(Optional<Transport> transport, String name, int index) {
        if(transport.isEmpty()){
            return false;
        }
        Controller.transport.set(index, new MyPair<>(name, transport.get()));
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
        showPluginPane();
    }
    @FXML
    private void onConfirm() {
        try {
            FileChooser chooser = new FileChooser();
            for(String extension : serializers.keySet()) {
                String extensions = "*" + extension;
                String info = serializers.get(extension).getExtension() + "(*" + extension;
                if(!pluginChoice.getValue().equals("None")) {
                    extensions = extensions + plugins.get(pluginChoice.getValue()).getExtension();
                    info = info + plugins.get(pluginChoice.getValue()).getExtension();
                }
                info = info + ")";
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                        serializers.get(extension).getExtension(), extensions));
            }
            File file = chooser.showOpenDialog(Application.stStage);
            if(file != null) {
                String extension = file.getName().substring(file.getName().lastIndexOf("."));
                if(isEncoded(file.getName())) {
                    extension = file.getName().substring(0, file.getName().lastIndexOf("."));
                    extension = extension.substring(extension.lastIndexOf("."));
                }
                Serializer serializer = serializers.get(extension);
                serializer.serialize(transport, file);
                if(!pluginChoice.getValue().equals("None")) {
                    encode(file);
                }
                hidePluginPane();
            }
        } catch (Exception e) {
            System.out.println("file not found");
        }
    }
    private void encode(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            byte[] data = stream.readAllBytes();
            data = plugins.get(pluginChoice.getValue()).encode(data);
            stream.close();
            FileOutputStream output = new FileOutputStream(file);
            output.write(data);
            output.close();
        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Не удалось применить плагин").showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Не удалось применить плагин").showAndWait();
        }
    }
    @FXML
    private void onOpen() {
        try {
            FileChooser chooser = new FileChooser();
            List<String> extensions = new ArrayList<>();
            for (String extension : serializers.keySet()) {
                String info = serializers.get(extension).getExtension() + "(*" + extension;
                extensions.clear();
                extensions.add("*" + extension);
                for(String name : plugins.keySet()) {
                    extensions.add("*" + extension + plugins.get(name).getExtension());
                    info = info + ", *" + plugins.get(name).getExtension();
                }
                info = info + ")";
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                        info, extensions));
                extensions.remove("*" + extension);
            }

            File file = chooser.showOpenDialog(Application.stStage);
            if(file != null) {
                byte[] data = null;
                String extension = file.getName().substring(file.getName().lastIndexOf("."));
                if(isEncoded(file.getName())) {
                    data = decode(file);
                    extension = file.getName().substring(0, file.getName().lastIndexOf("."));
                    extension = extension.substring(extension.lastIndexOf("."));
                }
                Serializer serializer = serializers.get(extension);
                ArrayList<MyPair<String, Transport>> transports = serializer.deserialize(file);
                for (MyPair<String, Transport> transport : transports) {
                    addTransport(Optional.of(transport.getValue()), transport.getKey());
                }
                if(isEncoded(file.getName())) {
                    FileOutputStream stream = new FileOutputStream(file);
                    stream.write(data);
                    stream.close();
                }
            }
        } catch (Exception e){
            System.out.println("file not found");
        }
    }
    private boolean isEncoded(String name) {
        for(String plugin : plugins.keySet()){
            if(plugins.get(plugin).getExtension().equals(name.substring(name.lastIndexOf(".")))) return true;
        }
        return false;
    }
    private byte[] decode(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            byte[] data = stream.readAllBytes();
            Plugin plugin = null;
            for(String name : plugins.keySet()) {
                if(plugins.get(name).getExtension().equals(file.getName().substring(file.getName().lastIndexOf(".")))){
                    plugin = plugins.get(name);
                    break;
                }
            }
            stream.close();
            FileOutputStream output = new FileOutputStream(file);
            output.write(plugin.decode(data));
            output.close();
            return data;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showPluginPane() {
        pluginChoice.getItems().clear();
        pluginChoice.getItems().add("None");
        pluginChoice.setValue("None");
        for(String plugin : plugins.keySet()) {
            pluginChoice.getItems().add(plugin);
        }
        pluginPane.setVisible(true);
    }
    @FXML
    private void hidePluginPane() {
        pluginPane.setVisible(false);
    }
    private void loadPlugins() {
        String path = "src/main/java/plugins";
        File dir = new File(path);
        for(File file : dir.listFiles()) {
            try {
                String pathToJar = file.getPath();
                JarFile jarFile = new JarFile(pathToJar);
                Enumeration<JarEntry> entry = jarFile.entries();

                URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
                URLClassLoader cl = URLClassLoader.newInstance(urls);

                while (entry.hasMoreElements()) {
                    JarEntry jarEntry = entry.nextElement();
                    if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                    className = className.replace('/', '.');
                    Plugin plugin = (Plugin) cl.loadClass(className).getConstructor().newInstance();
                    plugins.put(plugin.getName(), plugin);
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Не удалось загрузить плагины").showAndWait();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initFabrics();
        hidePluginPane();
        names = objectsList.getItems();
        types = objectsTypeList.getItems();
        serializers.put(".bin", new BinarySerializer());
        serializers.put(".json", new JsonSerializer());
        serializers.put(".txt", new TextSerializer());
        loadPlugins();
//        addTransport(Optional.of(new Car("bmw", "red", new Engine(20000, 6), "1234", Car.CarTypes.SPORTCAR, 5, 4)), "Моя машина");
//        addTransport(Optional.of(new Bicycle("stels", "blue", 24, 2, 1)), "Мой байк");
//        addTransport(Optional.of(new Bus("mercedes", "yellow", Bus.BusTypes.SCHOOL, new Engine(10000, 6), "5647", 20, 4)), "Школьный автобус");
    }
}