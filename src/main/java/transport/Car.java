package transport;

import GUI.Name;
import serializer.Serializer;

import java.io.Serializable;

@Name("Автомобиль")
public class Car extends MotorizedVehicle implements Serializable {
    public enum CarTypes{
        SEDAN, MINIVAN, SPORTCAR, HATCHBACK;
    }
    private CarTypes type;
    public Car(String brand, String color, Engine engine, String number, CarTypes type, int seats, int wheels){
        super(brand, number, engine, color, seats, wheels);
        this.type = type;
    }
    public Car() {}
    @Name("Тип кузова")
    public CarTypes getType() {
        return type;
    }
    @Name("Тип кузова")
    public void setType(CarTypes type) {
        this.type = type;
    }
}
