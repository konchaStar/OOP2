package serializer;

import by.oop.oop2.MyPair;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import transport.Transport;

import java.io.*;
import java.util.ArrayList;

public class BinarySerializer implements Serializer {
    @Override
    public String getExtension() {
        return "Binary file";
    }

    @Override
    public void serialize(ArrayList<MyPair<String, Transport>> transport, File file) {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(transport);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Не возможно записать объект в файл!").showAndWait();
        }
    }

    @Override
    public ArrayList<MyPair<String, Transport>> deserialize(File file) {
        ArrayList<MyPair<String, Transport>> transport = null;
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
            transport = (ArrayList<MyPair<String, Transport>>) stream.readObject();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Невозможно прочитать данные из файла!").showAndWait();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return transport;
    }
}
