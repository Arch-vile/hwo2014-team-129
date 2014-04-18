package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.carpositions.Lane;

/**
 * Tells in which track position we were in given game tick.
 * 
 */
public class TrackPosition {

    public final int gameTick;

    public final int pieceIndex;

    public final double inPieceDistance;

    public final Lane lane;

    public TrackPosition(int gameTick, int pieceIndex, double inPieceDistance,
            Lane lane) {
        this.gameTick = gameTick;
        this.pieceIndex = pieceIndex;
        this.inPieceDistance = inPieceDistance;
        this.lane = lane;
    }
}
