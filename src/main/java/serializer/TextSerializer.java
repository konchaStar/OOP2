package serializer;

import by.oop.oop2.MyPair;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import transport.Bus;
import transport.Car;
import transport.Engine;
import transport.Transport;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TextSerializer implements Serializer {
    @Override
    public String getExtension() {
        return "Text files";
    }

    @Override
    public void serialize(ArrayList<MyPair<String, Transport>> transport, File file) {
        try {
            BufferedWriter stream = new BufferedWriter(new FileWriter(file));
            for(MyPair<String, Transport> tr : transport) {
                StringBuilder builder = new StringBuilder();
                builder.append("Name:").append(tr.getKey()).append(";");
                builder.append("Type:").append(tr.getValue().getClass().getName()).append(";");
                Map<String, Method> getters = new HashMap<>();
                for(Method getter : Arrays.stream(tr.getValue().getClass().getMethods()).
                        filter(x -> x.getName().startsWith("get") && !x.getName().equals("getClass")).toList()){
                    getters.put(getter.getName().substring(getter.getName().indexOf("get") + 3), getter);
                }
                for(String field : getters.keySet()) {
                    builder.append(field).append(":");
                    if(field.equals("Engine")) {
                        builder.append("[").append(getters.get(field).invoke(tr.getValue())).append("]");
                    } else {

                        if(getters.get(field).getReturnType().getSimpleName().equals("String")) {
                            String value = (String) getters.get(field).invoke(tr.getValue());
                            value = hideParentheses(value);
                            builder.append(value);
                        } else {
                            builder.append(getters.get(field).invoke(tr.getValue()));
                        }
                    }
                    builder.append(";");
                }
                builder.append("\n");
                stream.write(builder.toString());
            }
            stream.close();
        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Невозможно сохранить в файл!").showAndWait();
        } catch (InvocationTargetException e) {
            new Alert(Alert.AlertType.ERROR, "Невозможно сохранить в файл!").showAndWait();
        } catch (IllegalAccessException e) {
            new Alert(Alert.AlertType.ERROR, "Невозможно сохранить в файл!").showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Невозможно сохранить в файл!").showAndWait();
        }
    }
    private String hideParentheses(String str) {
        String[] splitters = {"\\", ":", "[", ";", "]"};
        for(int i = 0; i < 5; i++) {
            int index = 0;
            while(str.indexOf(splitters[i], index) != -1) {
                StringBuilder builder = new StringBuilder(str);
                builder.insert(str.indexOf(splitters[i], index), "\\");
                str = builder.toString();
                index = str.indexOf(splitters[i], index) + 1  + (i == 0 ? 1 : 0);
            }
        }
        return str;
    }
    private String removeBackSlash(String str) {
        String[] splitters = {"\\:", "\\[", "\\;", "\\]", "\\\\"};
        for(int i = 0; i < 5; i++) {
            int index = 0;
            while(str.indexOf(splitters[i], index) != -1) {
                index = str.indexOf(splitters[i], index);
                StringBuilder builder = new StringBuilder(str);
                builder.replace(str.indexOf(splitters[i], index), str.indexOf(splitters[i], index) + 1, "");
                str = builder.toString();
                index++;
            }
        }
        return str;
    }
    @Override
    public ArrayList<MyPair<String, Transport>> deserialize(File file) {
        ArrayList<MyPair<String, Transport>> transports = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(reader.ready()) {
                String line = reader.readLine();
                int index = 0;
                Object transport = null;
                String name = "";
                HashMap<String, Method> setters = new HashMap<>();
                HashMap<String, String> types = new HashMap<>();
                while(index < line.length()) {
                    String part = "";
                    boolean skip = false;
                    int bs = 0;
                    while(line.toCharArray()[index] != ';' || (line.toCharArray()[index - 1] == '\\' && ((bs & 1) == 1)) || skip){
                        if(line.toCharArray()[index] == '[' && line.toCharArray()[index - 1] != '\\') {
                            skip = true;
                        }
                        if(line.toCharArray()[index] == ']' && line.toCharArray()[index - 1] != '\\'){
                            skip = false;
                        }
                        if(line.toCharArray()[index] != '\\' && bs != 0) {
                            bs = 0;
                        }
                        if(line.toCharArray()[index] == '\\'){
                            bs++;
                        }
                        part = part + line.toCharArray()[index];
                        index++;
                    }
                    String field = part.substring(0, part.indexOf(":"));
                    String value = part.substring(part.indexOf(":") + 1);
                    if(field.equals("Name")) {
                        name = value;
                    } else if(field.equals("Type")) {
                        transport = Class.forName(value).getConstructor().newInstance();
                        for(Method setter : Arrays.stream(Class.forName(value).getMethods()).filter(x ->
                                x.getName().startsWith("set")).toList()){
                            setters.put(setter.getName().substring(setter.getName().indexOf("set") + 3), setter);
                        }
                        for(Method getter : Arrays.stream(Class.forName(value).getMethods()).filter(x ->
                                x.getName().startsWith("get") && !x.getName().equals("getClass")).toList()){
                            types.put(getter.getName().substring(getter.getName().indexOf("get") + 3),
                                    getter.getReturnType().getSimpleName());
                        }
                    } else {
                        switch (types.get(field)) {
                            case "Engine" -> {
                                setters.get(field).invoke(transport, getEngine(value));
                            }
                            case "int" -> {
                                setters.get(field).invoke(transport, Integer.valueOf(value));
                            }
                            case "CarTypes" -> {
                                setters.get(field).invoke(transport, Car.CarTypes.valueOf(value));
                            }
                            case "BusTypes" -> {
                                setters.get(field).invoke(transport, Bus.BusTypes.valueOf(value));
                            }
                            case "String" -> {
                                setters.get(field).invoke(transport, removeBackSlash(value));
                            }
                        }
                    }
                    index++;
                }
                transports.add(new MyPair<>(name, (Transport) transport));
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Невозможно прочитать данные из файла!").showAndWait();
        }
        return transports;
    }
    private Engine getEngine(String data) throws Exception {
        Engine engine = new Engine();
        HashMap<String, Method> setters = new HashMap<>();
        for(Method setter : Arrays.stream(Engine.class.getMethods()).filter(x ->
                x.getName().startsWith("set")).toList()) {
            setters.put(setter.getName().substring(setter.getName().indexOf("set") + 3), setter);
        }
        int index = 1;
        while(data.toCharArray()[index] != ']') {
            String pair = "";
            while(data.toCharArray()[index] != ';') {
                pair = pair + data.toCharArray()[index];
                index++;
            }
            String field = pair.substring(0, pair.indexOf(":"));
            String value = pair.substring(pair.indexOf(":") + 1);
            setters.get(field).invoke(engine, Integer.parseInt(value));
            index++;
        }
        return engine;
    }
}
