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
    private BusTypes type;
    public Bus(String brand, String color, BusTypes type, Engine engine, String number, int seats, int wheels){
        super(brand, number, engine, color, seats, wheels);
        this.type = type;
    }
    @Name("Тип")
    public BusTypes getType() {
        return type;
    }
    @Name("Тип")
    public void setType(BusTypes type) {
        this.type = type;
    }

}
