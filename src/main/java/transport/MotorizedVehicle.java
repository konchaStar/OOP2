package transport;

import GUI.Name;

import java.io.Serializable;

public abstract class MotorizedVehicle extends Transport implements Serializable {
    protected String number;
    protected Engine engine;
    public MotorizedVehicle(String brand, String number, Engine engine, String color, int numberOfSeats, int wheelsAmount){
        super(wheelsAmount, numberOfSeats, number, brand);
        this.number = number;
        this.engine = engine;
    }
    @Name("Номер")
    public String getNumber() {
        return number;
    }
    @Name("Двигатель")
    public Engine getEngine() {
        return engine;
    }
    public MotorizedVehicle(){

    }
    @Name("Двигатель")
    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    @Name("Номер")
    public void setNumber(String number) {
        this.number = number;
    }
}
