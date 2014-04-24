package hwo.kurjatturskat.core.message.gameinit;

import hwo.kurjatturskat.core.message.CarIdentifier;

public class CarDimensions {
    public CarIdentifier id;
    public Dimensions dimensions;

    public CarDimensions(CarIdentifier car, Dimensions dimensions) {
        this.id = car;
        this.dimensions = dimensions;
    }
}
