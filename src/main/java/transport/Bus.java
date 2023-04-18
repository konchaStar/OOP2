package transport;

import GUI.Name;

import java.io.Serializable;


@Name("Автобус")
public class Bus extends MotorizedVehicle implements Serializable {
    public Bus() {

    }

    public enum BusTypes {
        SCHOOL, COACH, TRANSIT;
    }
    private BusTypes busType;
    public Bus(String brand, String color, BusTypes type, Engine engine, String number, int seats, int wheels){
        super(brand, number, engine, color, seats, wheels);
        this.busType = type;
    }
    @Name("Тип")
    public BusTypes getBusType() {
        return busType;
    }
    @Name("Тип")
    public void setBusType(BusTypes type) {
        this.busType = type;
    }

}
