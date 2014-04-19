package hwo.kurjatturskat.visualization;

import hwo.kurjatturskat.core.message.carpositions.PiecePosition;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;

public class PlotPoint {

    TrackPieces piece;
    PiecePosition position;

    public PlotPoint(TrackPieces piece, PiecePosition position) {
        this.piece = piece;
        this.position = position;
    }

}
