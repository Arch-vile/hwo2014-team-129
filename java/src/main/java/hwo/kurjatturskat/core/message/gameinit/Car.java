package hwo.kurjatturskat.core.message.gameinit;

import hwo.kurjatturskat.core.message.CarIdentifier;

public class Car {
    public CarIdentifier car;
    public Dimensions dimensions;

    public Car(CarIdentifier car, Dimensions dimensions) {
        this.car = car;
        this.dimensions = dimensions;
    }
}
