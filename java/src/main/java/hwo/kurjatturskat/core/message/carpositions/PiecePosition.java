package hwo.kurjatturskat.core.message.carpositions;

public class PiecePosition {

    public final int pieceIndex;
    public final double inPieceDistance;
    public final Lane lane;
    public final int lap;

    public PiecePosition(int pieceIndex, double inPieceDistance, Lane lane,
            int lap) {
        this.pieceIndex = pieceIndex;
        this.inPieceDistance = inPieceDistance;
        this.lane = lane;
        this.lap = lap;
    }
}
