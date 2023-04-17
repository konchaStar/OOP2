package transport;

import GUI.Name;
import fabrics.Number;
import serializer.Serializer;

import java.io.Serializable;

@Name("Грузовик")
public class Truck extends MotorizedVehicle implements Serializable {
    public Truck(){

    }
    private int maxWeight;
    public Truck(String brand, String color, Engine engine, String number, int maxWeight, int wheels, int seats){
        super(brand, number, engine, color, seats, wheels);
        this.maxWeight = maxWeight;
    }
    @Name("Максимальный вес")
    public int getMaxWeight() {
        return maxWeight;
    }
    @Number
    @Name("Максимальный вес")
    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }
}
