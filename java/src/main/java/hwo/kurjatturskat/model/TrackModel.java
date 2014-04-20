package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.gameinit.RaceSession;
import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.util.LoopingList;

import java.util.Arrays;
import java.util.List;

public class TrackModel {

    private LoopingList<TrackPieces> pieces;
    private TrackLanes[] lanes;
    private RaceSession raceSession;
    private String trackId = "";
    private String trackName = "";

    public TrackModel(TrackPieces pieces[], TrackLanes lanes[],
            RaceSession raceSession, String trackId, String trackName) {
        this.pieces = new LoopingList<>(Arrays.asList(pieces));
        this.lanes = lanes;
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

    // TODO: remove?
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

    public double getLaneDistanceBetweenPieces(int startPieceIndex,
            int endPieceIndex, int laneIndex) {

        int endIndex = endPieceIndex;

        if (endPieceIndex < startPieceIndex) {
            endIndex += (this.pieces.getAll().size());
        }
        double distance = 0.0;
        for (int i = startPieceIndex + 1; i < endIndex; i++) {
            distance += getLaneLengthOnPiece(i % this.pieces.getAll().size(),
                    laneIndex);
        }

        return distance;
    }
}
