package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.util.LoopingList;

import java.util.Arrays;
import java.util.List;

public class TrackModel {

    private LoopingList<TrackPieces> pieces;
    private String trackId = "";
    private String trackName = "";

    public TrackModel(TrackPieces pieces[], String trackId, String trackName) {
        this.pieces = new LoopingList<>(Arrays.asList(pieces));
    }

    public TrackPieces getCurrent() {
        return pieces.getCurrent();
    }

    public TrackPieces getNext() {
        return pieces.getNext();
    }

    public void setCurrentPiece(int yourPieceIndex) {
        pieces.setCurrent(yourPieceIndex);
    }

    public TrackPieces getPieceForIndex(int index) {
        return this.pieces.getByIndex(index);
    }

    public List<TrackPieces> getAll() {
        return pieces.getAll();
    }

    public String getTrackName() {
        return this.trackName;
    }

    public String getTrackId() {
        return this.trackId;
    }

}
