package fabrics;

import GUI.Name;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import transport.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BicycleFactory implements Factory{
    @Override
    public Optional<Transport> getTransport(HBox box) {
        Bicycle bicycle = new Bicycle();
        Class<? extends Transport> bicycleClass = Bicycle.class;
        Map<String, Method> setters = new HashMap<>();
        for(Method setter : Arrays.stream(bicycleClass.getMethods()).filter(x -> x.getName().indexOf("set") == 0).toList()){
            if(setter.isAnnotationPresent(Name.class)){
                setters.put(setter.getAnnotation(Name.class).value(), setter);
            }
        }
        VBox fieldsName = (VBox)box.getChildren().get(0);
        VBox fields = (VBox)box.getChildren().get(1);
        for(int i = 1; i < fieldsName.getChildren().size(); i++){
            Label label = (Label)fieldsName.getChildren().get(i);
            if(((TextField)fields.getChildren().get(i)).getText().equals("")){
                return Optional.empty();
            } else if(setters.get(label.getText()).isAnnotationPresent(Number.class)){
                try {
                    setters.get(label.getText()).invoke(bicycle, Integer.parseInt(((TextField)fields.getChildren().get(i)).getText()));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    setters.get(label.getText()).invoke(bicycle, ((TextField)fields.getChildren().get(i)).getText());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.of(bicycle);
    }

    @Override
    public void getFields(HBox box, Optional<? extends Transport> transport) {
        Class<?> bicycleClass = Bicycle.class;
        List<Method> methods = Arrays.stream(bicycleClass.getMethods()).filter(x -> x.getName().indexOf("get") == 0).toList();
        VBox namesContainer = (VBox) box.getChildren().get(0);
        VBox fieldsContainer = (VBox) box.getChildren().get(1);
        namesContainer.setSpacing(9);
        for(Method getter : methods){
            if(getter.isAnnotationPresent(Name.class)){
                namesContainer.getChildren().add(new Label(getter.getAnnotation(Name.class).value()));
                TextField field = new TextField();
                if(transport.isPresent()) {
                    try {
                        field.setText(getter.invoke((Bicycle)transport.get()).toString());
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
