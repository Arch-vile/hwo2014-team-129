package hwo.kurjatturskat.core.message.gameinit;

import hwo.kurjatturskat.core.message.CarIdentifier;

public class CarDimensions {
    public CarIdentifier car;
    public Dimensions dimensions;

    public CarDimensions(CarIdentifier car, Dimensions dimensions) {
        this.car = car;
        this.dimensions = dimensions;
    }
}
