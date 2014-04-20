package hwo.kurjatturskat.core.message;

public class CarIdentifier {

    public final String name;
    public final String color;

    CarIdentifier(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean isSameCar(CarIdentifier car) {
        return (this.name.equals(car.name) && this.color.equals(car.color));
    }
}
