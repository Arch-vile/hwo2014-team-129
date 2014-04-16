package hwo.kurjatturskat.core.message;

public class CarMsg {

    public final CarIdentifierMsg id;
    public final double angle;
    public final PiecePositionsMsg piecePositions;

    CarMsg(CarIdentifierMsg id, double angle, PiecePositionsMsg piecePositions) {
        this.id = id;
        this.angle = angle;
        this.piecePositions = piecePositions;
    }

}
