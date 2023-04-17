package serializer;

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
    public void serialize(ArrayList<Pair<String, Transport>> transport, File file) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();
        Type type = new TypeToken<ArrayList<Pair<String, Transport>>>(){}.getType();
        try {
            FileWriter stream = new FileWriter(file);
            String json = gson.toJson(transport, type);
            stream.write(json);
            stream.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Невозможно записать в файл!").showAndWait();
        }
    }

    @Override
    public ArrayList<Pair<String, Transport>> deserialize(File file) {
        return null;
    }
}
