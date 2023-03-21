package transport;

import GUI.Name;
import fabrics.Number;

public class Engine {
    private int power;
    private int pistonsAmount;
    public Engine(int power, int pistons){
        this.pistonsAmount = pistons;
        this.power = power;
    }
    public Engine() {

    }
    @Name("Мощность")
    public int getPower() {
        return power;
    }
    @Name("Число поршней")
    public int getPistonsAmount() {
        return pistonsAmount;
    }
    @Number
    @Name("Мощность")
    public void setPower(int power) {
        this.power = power;
    }
    @Number
    @Name("Число поршней")
    public void setPistonsAmount(int pistonsAmount) {
        this.pistonsAmount = pistonsAmount;
    }
}
