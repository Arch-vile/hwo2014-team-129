package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.CrashMsg;
import hwo.kurjatturskat.core.message.SpawnMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPosition;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.carpositions.PiecePosition;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.lapfinished.LapFinishedMsg;
import hwo.kurjatturskat.core.message.lapfinished.LapTime;
import hwo.kurjatturskat.core.message.lapfinished.RaceTime;
import hwo.kurjatturskat.util.Physics;

import java.util.ArrayList;

public class World {

    private TrackModel trackModel;
    private CarIdentifier myCar;
    private RaceTime myRaceTime;
    private LapTime myLapTimes[];
    private double recordSpeed = 0;
    private double speed;
    private boolean onTrack;

    private ArrayList<TrackPosition> myCarTravels = new ArrayList<TrackPosition>();

    private TrackPosition previousPosition;

    private TrackLanes[] lanes;

    public Physics myPhysics;

    public void update(CarPositionsMsg msg) {
        CarPosition myCarPosition = this.getCarPositionForCar(msg.getData(),
                this.myCar);
        if (myCarPosition == null) {
            return;
        }
        PiecePosition myPiecePos = myCarPosition.piecePosition;
        trackModel.setCurrentPiece(myPiecePos.pieceIndex);
        TrackPosition trackPos = new TrackPosition(msg.gameTick,
                myPiecePos.pieceIndex, myPiecePos.inPieceDistance,
                myPiecePos.lane, trackModel.getCurrent(), myCarPosition.angle);

        this.myPhysics.addTrackPosition(trackPos);
        // this.myCarTravels.add(trackPos);

        this.speed = this.myPhysics.getCurrentSpeed();

        if (this.speed > this.recordSpeed) {
            this.recordSpeed = this.speed;
        }
        this.previousPosition = trackPos;
        // System.out.println("Speed (" + myPiecePos.pieceIndex + ", distance "
        // + this.distanceTraveled + "): " + speed);
    }

    public RaceTime getMyRaceTime() {
        return this.myRaceTime;
    }

    public double getMyRecordSpeed() {
        return this.recordSpeed;
    }

    public double getMySpeed() {
        return this.speed;
    }

    public int getMyLapTime(int lap) {
        return this.myLapTimes[lap - 1].millis;
    }

    public void update(GameInitMsg message) {
        this.trackModel = new TrackModel(message.getData().race.track.pieces,
                message.getData().race.track.lanes,
                message.getData().race.raceSession,
                message.getData().race.track.id,
                message.getData().race.track.name);
        this.lanes = message.getData().race.track.lanes;
        this.myPhysics = new Physics(this.lanes);
        this.myRaceTime = new RaceTime();
        this.myLapTimes = new LapTime[message.getData().race.raceSession.laps];
        for (int n = 0; n < message.getData().race.raceSession.laps; n++) {
            this.myLapTimes[n] = new LapTime();
        }
    }

    public void update(LapFinishedMsg message) {
        if (this.myCar.isSameCar(message.getData().car)) {
            this.myLapTimes[message.getData().lapTime.lap].update(message
                    .getData().lapTime);
            this.myRaceTime.update(message.getData().raceTime);
        }

    }

    public void setMyCarId(CarIdentifier carId) {
        this.myCar = carId;
        this.onTrack = true;
    }

    public boolean onTrack() {
        return this.onTrack;
    }

    /**
     * Retrieve the car position for given car.
     * 
     * @param data
     * @param carId
     * @return
     */
    private CarPosition getCarPositionForCar(CarPosition[] data,
            CarIdentifier carId) {
        for (CarPosition posData : data) {
            CarIdentifier id = posData.id;
            if (carId.color.equals(id.color)) {
                return posData;
            }
        }
        System.err.println("Could not determine yourself from car positions!");
        return null;
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
        CarPosition position = this.getCarPositionForCar(data, carId);
        return position.piecePosition;
    }

    public TrackModel getTrackModel() {
        return trackModel;
    }

    public boolean isInitialized() {
        return this.trackModel != null && this.myCar != null;
    }

    public void setOffTrack(CrashMsg message) {
        if (this.myCar.name.equals(message.getData().name)
                && this.myCar.color.equals(message.getData().color)) {
            System.out.println("We crashed!");
            this.onTrack = false;
        }
    }

    public void setOnTrack(SpawnMsg message) {
        if (this.myCar.name.equals(message.getData().name)
                && this.myCar.color.equals(message.getData().color)) {
            System.out.println("We are back on track!");
            this.onTrack = true;
        }
    }

    public TrackLanes getLane(int index) {
        for (TrackLanes lane : this.lanes) {
            if (index == lane.index) {
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
            if (lane.index == getPreviousPosition().lane.startLaneIndex) {
                return lane;
            }
        }

        return null;
    }

}
