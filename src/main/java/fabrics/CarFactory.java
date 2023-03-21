package fabrics;

import GUI.Name;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import transport.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CarFactory implements Factory {
    @Override
    public Optional<Transport> getTransport(HBox box) {
        Car car = new Car();
        Class<? extends Transport> carClass = Car.class;
        Map<String, Method> setters = new HashMap<>();
        for(Method setter : Arrays.stream(carClass.getMethods()).filter(x -> x.getName().indexOf("set") == 0).toList()){
            if(setter.isAnnotationPresent(Name.class)){
                setters.put(setter.getAnnotation(Name.class).value(), setter);
            }
        }
        VBox fieldsName = (VBox)box.getChildren().get(0);
        VBox fields = (VBox)box.getChildren().get(1);
        for(int i = 1; i < fieldsName.getChildren().size(); i++){
            Label label = (Label)fieldsName.getChildren().get(i);
            if(label.getText().equals("Тип кузова")){
                switch(((ChoiceBox<String>)fields.getChildren().get(i)).getValue()){
                    case "Седан"->{
                        try {
                            setters.get(label.getText()).invoke(car, Car.CarTypes.SEDAN);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    case "Хетчбэк"->{
                        try {
                            setters.get(label.getText()).invoke(car, Car.CarTypes.HATCHBACK);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    case "Спорткар"->{
                        try {
                            setters.get(label.getText()).invoke(car, Car.CarTypes.SPORTCAR);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    case "Минивэн"->{
                        try {
                            setters.get(label.getText()).invoke(car, Car.CarTypes.MINIVAN);
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
                    setters.get(label.getText()).invoke(car, engine);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else if(((TextField)fields.getChildren().get(i)).getText().equals("")){
                return Optional.empty();
            } else if(setters.get(label.getText()).isAnnotationPresent(Number.class)){
                try {
                    setters.get(label.getText()).invoke(car, Integer.parseInt(((TextField)fields.getChildren().get(i)).getText()));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    setters.get(label.getText()).invoke(car, ((TextField)fields.getChildren().get(i)).getText());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.of(car);
    }

    @Override
    public void getFields(HBox box, Optional<? extends Transport> transport) {
        Class<?> carClass = Car.class;
        List<Method> methods = Arrays.stream(carClass.getMethods()).filter(x -> x.getName().indexOf("get") == 0).toList();
        VBox namesContainer = (VBox) box.getChildren().get(0);
        VBox fieldsContainer = (VBox) box.getChildren().get(1);
        namesContainer.setSpacing(9);
        for(Method getter : methods){
            if(getter.isAnnotationPresent(Name.class)){
                namesContainer.getChildren().add(new Label(getter.getAnnotation(Name.class).value()));
                if(getter.getReturnType().equals(Car.CarTypes.class)){
                    ChoiceBox<String> choiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(
                            "Седан", "Минивэн", "Спорткар", "Хетчбэк"
                    ));
                    if(transport.isPresent()) {
                        try {
                            Car.CarTypes type = (Car.CarTypes) getter.invoke((Car) transport.get());
                            if (type == Car.CarTypes.HATCHBACK) {
                                choiceBox.setValue("Хетчбэк");
                            }
                            if (type == Car.CarTypes.SPORTCAR) {
                                choiceBox.setValue("Спорткар");
                            }
                            if (type == Car.CarTypes.SEDAN) {
                                choiceBox.setValue("Седан");
                            }
                            if (type == Car.CarTypes.MINIVAN) {
                                choiceBox.setValue("Минивэн");
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
                                    field.setText(method.invoke(getter.invoke((Car)transport.get())).toString());
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
                            field.setText(getter.invoke((Car)transport.get()).toString());
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
