package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.carpositions.Lane;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;

/**
 * Tells in which track position we were in given game tick.
 * 
 */
public class TrackPosition {

    public final int gameTick;

    public final int pieceIndex;

    public final double inPieceDistance;

    public final Lane lane;

    private final TrackPieces trackPiece;

    public final double carAngle;

    public TrackPosition(int gameTick, int pieceIndex, double inPieceDistance,
            Lane lane, TrackPieces trackPiece, double carAngle) {
        this.gameTick = gameTick;
        this.pieceIndex = pieceIndex;
        this.inPieceDistance = inPieceDistance;
        this.lane = lane;
        this.trackPiece = trackPiece;
        this.carAngle = carAngle;
    }

    public TrackPieces getTrackPiece() {
        return trackPiece;
    }

}
