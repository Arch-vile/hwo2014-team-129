package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.gameinit.RaceSession;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.util.LoopingList;

import java.util.Arrays;
import java.util.List;

public class TrackModel {

    private LoopingList<TrackPieces> pieces;
    private RaceSession raceSession;
    private String trackId = "";
    private String trackName = "";

    public TrackModel(TrackPieces pieces[], RaceSession raceSession,
            String trackId, String trackName) {
        this.pieces = new LoopingList<>(Arrays.asList(pieces));
        this.raceSession = raceSession;
        this.trackId = trackId;
        this.trackName = trackName;
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

    public int getLaps() {
        return this.raceSession.laps;
    }

    /*
     * Get the next switch piece index starting from current index.
     */
    public int getNextSwitch() {
        return getNextSwitchByIndex(this.pieces.getCurrentIndex());
    }

    /*
     * Get the next switch piece index starting from given index.
     */
    public int getNextSwitchByIndex(int index) {
        index += 1;
        while (!this.pieces.getByIndex(index).isSwitch) {
            index++;
        }
        return index % this.pieces.getAll().size();
    }

}
