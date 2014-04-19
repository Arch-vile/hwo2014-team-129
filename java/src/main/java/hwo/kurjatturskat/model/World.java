package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.CrashMsg;
import hwo.kurjatturskat.core.message.SpawnMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPosition;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.carpositions.PiecePosition;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.util.TrackUtils;

import java.util.ArrayList;

public class World {

    private TrackModel trackModel;
    private CarIdentifier yourCar;
    private double recordSpeed = 0;
    private boolean onTrack;
    private double speed;

    private ArrayList<TrackPosition> myCarTravels = new ArrayList<TrackPosition>();

    private TrackPosition previousPosition;

    private TrackLanes[] lanes;

    public void update(CarPositionsMsg msg) {
        PiecePosition myPiecePos = this.getPiecePositionForCar(msg.getData(),
                this.yourCar);
        trackModel.setCurrentPiece(myPiecePos.pieceIndex);
        TrackPosition trackPos = new TrackPosition(msg.gameTick,
                myPiecePos.pieceIndex, myPiecePos.inPieceDistance,
                myPiecePos.lane, trackModel.getCurrent());

        // this.myCarTravels.add(trackPos);

        if (this.previousPosition != null) {
            this.speed = this.getSpeed(this.previousPosition, trackPos);

            if (speed > this.recordSpeed) {
                this.recordSpeed = speed;
                // if (this.recordSpeed > 8) {
                // System.out.println("New recordSpeed: " + this.recordSpeed);
                // }
            }
        }
        this.previousPosition = trackPos;
        // System.out.println("Speed (" + myPiecePos.pieceIndex + ", distance "
        // + this.distanceTraveled + "): " + speed);
    }

    public double getMyRecordSpeed() {
        return this.recordSpeed;
    }

    public double getMySpeed() {
        return this.speed;
    }

    public void update(GameInitMsg message) {
        this.trackModel = new TrackModel(message.getData().race.track.pieces,
                message.getData().race.track.id,
                message.getData().race.track.name);
        this.lanes = message.getData().race.track.lanes;
    }

    public void setYourCarId(CarIdentifier carId) {
        this.yourCar = carId;
        this.onTrack = true;
    }

    public boolean onTrack() {
        return this.onTrack;
    }

    /**
     * Retrieves the piecePosition for given car identifier from car position
     * data.
     * 
     * @param data
     * @param carId
     * @return Null is returned if we have no position data for given car id.
     */
    private PiecePosition getPiecePositionForCar(CarPosition[] data,
            CarIdentifier carId) {
        for (CarPosition posData : data) {
            CarIdentifier id = posData.id;
            if (carId.color.equals(id.color)) {
                return posData.piecePosition;

            }
        }
        System.err.println("Could not determine yourself from car positions!");
        return null;
    }

    public TrackModel getTrackModel() {
        return trackModel;
    }

    public boolean isInitialized() {
        return this.trackModel != null && this.yourCar != null;
    }

    public void setOffTrack(CrashMsg message) {
        if (this.yourCar.name.equals(message.getData().name)
                && this.yourCar.color.equals(message.getData().color)) {
            System.out.println("We crashed!");
            this.onTrack = false;
        }
    }

    public void setOnTrack(SpawnMsg message) {
        if (this.yourCar.name.equals(message.getData().name)
                && this.yourCar.color.equals(message.getData().color)) {
            System.out.println("We are back on track!");
            this.onTrack = true;
        }
    }

    /**
     * Retrieve the speed between two track positions.
     * 
     * @param start
     * @param end
     * @return
     */
    public double getSpeed(TrackPosition start, TrackPosition end) {
        int startTime = start.gameTick;
        int endTime = end.gameTick;
        TrackLanes startLane = getLane(start.lane.endLaneIndex);

        double distance = TrackUtils.getDistance(start, end, startLane);
        return distance / (endTime - startTime);
    }

    public TrackLanes getLane(int index) {
        for (TrackLanes lane : this.lanes) {
            if (index == lane.id) {
                return lane;
            }
        }
        return null;
    }

    public TrackPosition getPreviousPosition() {
        return previousPosition;
    }

    public TrackLanes[] getLanes() {
        return lanes;
    }

    public TrackLanes getMyLane() {
        for (TrackLanes lane : getLanes()) {
            if (lane.id == getPreviousPosition().lane.startLaneIndex) {
                return lane;
            }
        }

        return null;
    }

}
