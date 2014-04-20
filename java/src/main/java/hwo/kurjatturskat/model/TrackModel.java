package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.gameinit.RaceSession;
import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.util.LoopingList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackModel {

    private LoopingList<TrackPieces> pieces;
    private TrackLanes[] lanes;
    private RaceSession raceSession;
    private String trackId = "";
    private String trackName = "";
    private TrackPieces myCurrentTrackPiece;

    public TrackModel(TrackPieces pieces[], TrackLanes lanes[],
            RaceSession raceSession, String trackId, String trackName) {

        ArrayList<TrackPieces> pieceList = new ArrayList<>();
        pieceList.addAll(Arrays.asList(pieces));

        this.pieces = new LoopingList<>(pieceList);
        this.lanes = lanes;
        this.raceSession = raceSession;
        this.trackId = trackId;
        this.trackName = trackName;
        this.myCurrentTrackPiece = pieces[0];
    }

    public TrackPieces getCurrent() {
        return myCurrentTrackPiece;
    }

    public TrackPieces getNext() {
        return pieces.getNext();
    }

    public void setMyCurrentTrackPiece(TrackPieces piece) {
        this.myCurrentTrackPiece = piece;
    }

    public void setMyCurrentTrackPiece(int pieceIndex) {
        this.myCurrentTrackPiece = getPieceForIndex(pieceIndex);
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

    // TODO: remove?
    public int getLaps() {
        return this.raceSession.laps;
    }

    /*
     * Get the next switch piece index starting from current index.
     */

    public TrackPieces getNextSwitch() {
        return getNextSwitchByIndex(this.myCurrentTrackPiece);
    }

    /*
     * Get the next switch piece index starting from given piece (exclusive).
     */
    public TrackPieces getNextSwitchByIndex(TrackPieces from) {
        this.pieces.setCurrent(from);
        this.pieces.advance();
        while (!this.pieces.getCurrent().isSwitch) {
            this.pieces.advance();
        }
        return this.pieces.getCurrent();
    }

    public TrackLanes getLane(int index) {
        for (TrackLanes lane : this.lanes) {
            if (index == lane.index) {
                return lane;
            }
        }
        return null;
    }

    public TrackLanes getLaneForIndex(int laneIndex) {
        for (int i = 0; i < this.lanes.length; i++) {
            if (this.lanes[i].index == laneIndex) {
                return this.lanes[i];
            }
        }
        return null;
    }

    public double getLaneLengthOnPiece(int pieceIndex, int laneIndex) {

        double distance = 0.0;
        TrackPieces piece = this.getPieceForIndex(pieceIndex);
        TrackLanes lane = this.getLaneForIndex(laneIndex);

        if (piece.isCurve()) {
            double ourLaneOffset = 0.0;
            ourLaneOffset = lane.distanceFromCenter;
            if (piece.angle < 0) {
                ourLaneOffset *= -1;
            }

            distance += ((Math.abs(piece.angle) / 360) * 2 * Math.PI)
                    * (piece.radius - ourLaneOffset);
        } else {
            distance += piece.length;
        }

        return distance;
    }

    /*
     * Get distance between to pieces, excluding end pieces.
     */

    public double getLaneDistanceBetweenPieces(TrackPieces startPiece,
            TrackPieces endPiece, int laneIndex) {

        int endIndex = getIndexForPiece(endPiece);

        if (getIndexForPiece(endPiece) < getIndexForPiece(startPiece)) {
            endIndex += (this.pieces.getAll().size());
        }
        double distance = 0.0;
        for (int i = getIndexForPiece(startPiece) + 1; i < endIndex; i++) {
            distance += getLaneLengthOnPiece(i % this.pieces.getAll().size(),
                    laneIndex);
        }

        return distance;
    }

    private int getIndexForPiece(TrackPieces piece) {
        return this.pieces.getIndex(piece);
    }

}
