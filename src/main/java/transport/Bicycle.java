package transport;

import GUI.Name;
import fabrics.Number;

@Name("Велосипед")
public class Bicycle extends Transport{
    public Bicycle(){

    }
    private int wheelsDiameter;
    public Bicycle(String brand, String color, int diameter, int wheels, int seats){
        super(wheels, seats, color, brand);
        this.wheelsDiameter = diameter;
    }
    @Name("Диаметр колёс")
    public int getWheelsDiameter() {
        return wheelsDiameter;
    }
    @Name("Диаметр колёс")
    @Number
    public void setWheelsDiameter(int wheelsDiameter) {
        this.wheelsDiameter = wheelsDiameter;
    }

}
