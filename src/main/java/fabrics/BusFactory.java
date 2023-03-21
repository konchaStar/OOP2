package fabrics;

import GUI.Name;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import transport.Bus;
import transport.Car;
import transport.Engine;
import transport.Transport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BusFactory implements Factory {
    @Override
    public Optional<Transport> getTransport(HBox box) {
        Bus bus = new Bus();
        Class<? extends Transport> busClass = Bus.class;
        Map<String, Method> setters = new HashMap<>();
        for(Method setter : Arrays.stream(busClass.getMethods()).filter(x -> x.getName().indexOf("set") == 0).toList()){
            if(setter.isAnnotationPresent(Name.class)){
                setters.put(setter.getAnnotation(Name.class).value(), setter);
            }
        }
        VBox fieldsName = (VBox)box.getChildren().get(0);
        VBox fields = (VBox)box.getChildren().get(1);
        for(int i = 1; i < fieldsName.getChildren().size(); i++){
            Label label = (Label)fieldsName.getChildren().get(i);
            if(label.getText().equals("Тип")){
                switch(((ChoiceBox<String>)fields.getChildren().get(i)).getValue()){
                    case "Школьный"->{
                        try {
                            setters.get(label.getText()).invoke(bus, Bus.BusTypes.SCHOOL);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    case "Городской"->{
                        try {
                            setters.get(label.getText()).invoke(bus, Bus.BusTypes.TRANSIT);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    case "Междугородний"->{
                        try {
                            setters.get(label.getText()).invoke(bus, Bus.BusTypes.COACH);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    default->{
                        return Optional.empty();
                    }
                }
            } else if(label.getText().equals("Двигатель")){
                HashMap<String, Method> engineSetters = new HashMap<>();
                Engine engine = new Engine();
                for(Method setter : Arrays.stream(Engine.class.getMethods()).filter(x -> x.getName().startsWith("set")).toList()) {
                    if(setter.isAnnotationPresent(Name.class)){
                        engineSetters.put(setter.getAnnotation(Name.class).value(), setter);
                    }
                }
                HBox engineBox = (HBox)fields.getChildren().get(i);
                for(int j = 0; j < engineBox.getChildren().size() / 2; j++){
                    try {
                        engineSetters.get(((Label)engineBox.getChildren().get(j * 2)).getText()).invoke(engine,
                                Integer.parseInt(((TextField)engineBox.getChildren().get(j * 2 + 1)).getText()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    setters.get(label.getText()).invoke(bus, engine);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else if(((TextField)fields.getChildren().get(i)).getText().equals("")){
                return Optional.empty();
            } else if(setters.get(label.getText()).isAnnotationPresent(Number.class)){
                try {
                    setters.get(label.getText()).invoke(bus, Integer.parseInt(((TextField)fields.getChildren().get(i)).getText()));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    setters.get(label.getText()).invoke(bus, ((TextField)fields.getChildren().get(i)).getText());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.of(bus);
    }

    @Override
    public void getFields(HBox box, Optional<? extends Transport> transport) {
        Class<?> busClass = Bus.class;
        List<Method> methods = Arrays.stream(busClass.getMethods()).filter(x -> x.getName().indexOf("get") == 0).toList();
        VBox namesContainer = (VBox) box.getChildren().get(0);
        VBox fieldsContainer = (VBox) box.getChildren().get(1);
        namesContainer.setSpacing(9);
        for(Method getter : methods){
            if(getter.isAnnotationPresent(Name.class)){
                namesContainer.getChildren().add(new Label(getter.getAnnotation(Name.class).value()));
                if(getter.getReturnType().equals(Bus.BusTypes.class)){
                    ChoiceBox<String> choiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(
                            "Школьный", "Городской", "Междугородний"
                    ));
                    if(transport.isPresent()) {
                        try {
                            Bus.BusTypes type = (Bus.BusTypes) getter.invoke((Bus) transport.get());
                            if (type == Bus.BusTypes.SCHOOL) {
                                choiceBox.setValue("Школьный");
                            }
                            if (type == Bus.BusTypes.TRANSIT) {
                                choiceBox.setValue("Городской");
                            }
                            if (type == Bus.BusTypes.COACH) {
                                choiceBox.setValue("Междугородний");
                            }

                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        choiceBox.setValue(choiceBox.getItems().get(0));
                    }
                    fieldsContainer.getChildren().add(choiceBox);
                } else if(getter.getReturnType().equals(Engine.class)){
                    HBox engineBox = new HBox();
                    for(Method method : Arrays.stream(Engine.class.getMethods()).filter(x -> x.getName().startsWith("get")).toList()){
                        if(method.isAnnotationPresent(Name.class)){
                            engineBox.getChildren().add(new Label(method.getAnnotation(Name.class).value()));
                            TextField field = new TextField();
                            if(transport.isPresent()){
                                try {
                                    field.setText(method.invoke(getter.invoke((Bus)transport.get())).toString());
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                            engineBox.getChildren().add(field);
                        }
                    }
                    fieldsContainer.getChildren().add(engineBox);
                } else {
                    TextField field = new TextField();
                    if(transport.isPresent()) {
                        try {
                            field.setText(getter.invoke((Bus)transport.get()).toString());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    fieldsContainer.getChildren().add(field);
                }
            }
        }
    }
}
