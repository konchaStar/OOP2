package transport;

import GUI.Name;
import fabrics.Number;

@Name("Моторная лодка")
public class MotorizedBoat extends MotorizedVehicle{
    private int propellerAmount;
    public MotorizedBoat(String brand, String color, int propeller, Engine engine, String number, int seats){
        super(brand, number, engine, color, seats, 0);
        this.propellerAmount = propeller;
    }
    public MotorizedBoat() {}
    @Name("Число пропеллеров")
    public int getPropellerAmount() {
        return propellerAmount;
    }
    @Number
    @Name("Число пропеллеров")
    public void setPropellerAmount(int propellerAmount) {
        this.propellerAmount = propellerAmount;
    }
}
