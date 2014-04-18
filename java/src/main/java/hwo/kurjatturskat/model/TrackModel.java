package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.util.LoopingList;

import java.util.Arrays;

public class TrackModel {

    private LoopingList<TrackPieces> pieces;

    public TrackModel(TrackPieces pieces[]) {
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

}
