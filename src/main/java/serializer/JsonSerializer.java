package serializer;

import by.oop.oop2.MyPair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import transport.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.typeadapters.*;

public class JsonSerializer implements Serializer {
    private final RuntimeTypeAdapterFactory<Transport> typeAdapterFactory = RuntimeTypeAdapterFactory.of(Transport.class, "type").
    registerSubtype(Car.class, "car").registerSubtype(Truck.class, "truck").registerSubtype(Bicycle.class, "bicycle").
            registerSubtype(MotorizedBoat.class, "motorizedBoat").registerSubtype(Bus.class, "bus");

    @Override
    public String getExtension() {
        return "JSON files(*.json)";
    }

    @Override
    public void serialize(ArrayList<MyPair<String, Transport>> transport, File file) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();
        //Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MyPair<String, Transport>>>(){}.getType();
        try {
            FileWriter stream = new FileWriter(file);
            String json = gson.toJson(transport, type);
            stream.write(json);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Невозможно записать в файл!").showAndWait();
        }
    }

    @Override
    public ArrayList<MyPair<String, Transport>> deserialize(File file) {
        Gson gson = new Gson().newBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();
        Type type = new TypeToken<ArrayList<MyPair<String, Transport>>>(){}.getType();
        String json = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            json = bufferedReader.readLine();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Невозможно прочитать данные из файла!");
        }
        return gson.fromJson(json, type);
    }
}
