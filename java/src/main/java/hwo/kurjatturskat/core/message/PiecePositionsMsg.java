package hwo.kurjatturskat.core.message;

public class PiecePositionsMsg {

    public final int pieceIndex;
    public final double inPieceDistance;
    public final LaneMsg lane;
    public final int lap;

    public PiecePositionsMsg(int pieceIndex, double inPieceDistance,
            LaneMsg lane, int lap) {
        this.pieceIndex = pieceIndex;
        this.inPieceDistance = inPieceDistance;
        this.lane = lane;
        this.lap = lap;
    }
}
