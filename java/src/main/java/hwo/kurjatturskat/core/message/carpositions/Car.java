package hwo.kurjatturskat.core.message.carpositions;

import hwo.kurjatturskat.core.message.CarIdentifier;

public class Car {

    public final CarIdentifier id;
    public final double angle;
    public final PiecePosition piecePosition;

    Car(CarIdentifier id, double angle, PiecePosition piecePosition) {
        this.id = id;
        this.angle = angle;
        this.piecePosition = piecePosition;
    }

}
