package transport;

import GUI.Name;
import fabrics.Number;

import java.io.Serializable;

public abstract class Transport implements Serializable {
    protected int numberOfSeats;
    protected String color;
    protected String brand;

    public Transport() {

    }

    public Transport(int wheelsAmount, int numberOfSeats, String color, String brand) {
        this.numberOfSeats = numberOfSeats;
        this.color = color;
        this.brand = brand;
    }
    @Name("Число сидений")
    public int getNumberOfSeats() {
        return numberOfSeats;
    }
    @Name("Цвет")
    public String getColor() {
        return color;
    }
    @Name("Марка")
    public String getBrand() {
        return brand;
    }
    @Name("Марка")
    public void setBrand(String brand) {
        this.brand = brand;
    }
    @Name("Цвет")
    public void setColor(String color) {
        this.color = color;
    }
    @Number
    @Name("Число сидений")
    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

}

