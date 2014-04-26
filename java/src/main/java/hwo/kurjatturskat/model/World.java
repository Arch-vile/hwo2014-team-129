package hwo.kurjatturskat.model;

import hwo.kurjatturskat.ai.drivers.Driver;
import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.CrashMsg;
import hwo.kurjatturskat.core.message.SpawnMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPosition;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.carpositions.Lane;
import hwo.kurjatturskat.core.message.carpositions.PiecePosition;
import hwo.kurjatturskat.core.message.gameend.GameEndBestLaps;
import hwo.kurjatturskat.core.message.gameend.GameEndMsg;
import hwo.kurjatturskat.core.message.gameend.GameEndResults;
import hwo.kurjatturskat.core.message.gameinit.CarDimensions;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.lapfinished.LapFinishedMsg;
import hwo.kurjatturskat.core.message.turbo.TurboEndMsg;
import hwo.kurjatturskat.core.message.turbo.TurboStartMsg;
import hwo.kurjatturskat.core.message.turboavailable.TurboAvailable;
import hwo.kurjatturskat.core.message.turboavailable.TurboAvailableMsg;
import hwo.kurjatturskat.util.Physics;

import java.util.ArrayList;

public class World {
    private String botName;
    private TrackModel trackModel;
    private CarIdentifier myCar;
    private double recordSpeed = 0;
    private double speed;
    private boolean onTrack;
    private boolean myTurboOn;

    private ArrayList<TrackPosition> myCarTravels = new ArrayList<TrackPosition>();

    private TrackPosition previousPosition;
    private CarPosition opponentPositions[];

    private TrackLanes[] lanes;

    public Physics myPhysics;

    private String gameStatus;
    private GameResults gameResults;
    private LapResults lapResults;

    private TurboAvailable turbo;

    private Driver driver;

    public World(Driver driver, String botName) {
        this.botName = botName;
        this.gameStatus = "";
        this.gameResults = null;
        this.driver = driver;
        this.myTurboOn = false;
    }

    public String getMyBotName() {
        return this.botName;
    }

    public void update(CarPositionsMsg msg) {
        CarPosition myCarPosition = this.getCarPositionForCar(msg.getData(),
                this.myCar);
        if (myCarPosition == null) {
            return;
        }
        PiecePosition myPiecePos = myCarPosition.piecePosition;
        this.trackModel.setMyCurrentTrackPiece(myPiecePos.pieceIndex);
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

        // Update all car positions on track
        this.opponentPositions = msg.getData();
    }

    public double getMyRecordSpeed() {
        return this.recordSpeed;
    }

    public double getMySpeed() {
        return this.speed;
    }

    public void update(GameInitMsg message) {
        this.trackModel = new TrackModel(message.getData().race.track.pieces,
                message.getData().race.track.lanes,
                message.getData().race.raceSession,
                message.getData().race.track.id,
                message.getData().race.track.name);
        this.lanes = message.getData().race.track.lanes;
        this.myPhysics = new Physics(this.lanes, this.driver);
        // check for my car
        for (CarDimensions carDimensions : message.getData().race.cars) {
            CarIdentifier car = carDimensions.id;
            if (car != null && car.color.equals(this.myCar.color)) {
                this.myPhysics.setCarDimensions(
                        carDimensions.dimensions.length,
                        carDimensions.dimensions.flag);
                break;
            }
        }

        this.lapResults = new LapResults(message.getData().race.raceSession);
        if (message.getData().race.raceSession.isQualifying()) {
            this.gameStatus = "Qualifying";
        } else {
            this.gameStatus = "Race";
        }
    }

    public void update(LapFinishedMsg message) {
        if (this.myCar.isSameCar(message.getData().car)) {
            this.lapResults.lapFinished(message.getData());
        }

    }

    // TODO: move all the non update stuff away
    public void update(TurboAvailableMsg message) {
        this.turbo = message.getData();
    }

    public void update(GameEndMsg message) {
        this.gameResults = new GameResults(message.getData());
        System.out.println("");
        System.out.println(this.gameStatus + " end!");
        for (GameEndResults res : this.gameResults.getGameEndResults()) {
            System.out.println(res.car.name + "/" + res.car.color + " Laps: "
                    + res.result.laps + " Time: " + res.result.getRaceTime());
        }
        System.out.println("Best lap times!");
        for (GameEndBestLaps res : this.gameResults.getGameEndBestLaps()) {
            System.out.println(res.car.name + "/" + res.car.color + " Lap: "
                    + res.result.lap + " @ " + res.result.getLapTime());
        }
        System.out.println("");
    }

    public TurboAvailable getTurbo() {
        return turbo;
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
            this.myTurboOn = false;
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
        return this.previousPosition;
    }

    public TrackLanes[] getLanes() {
        return this.lanes;
    }

    public TrackLanes getMyLane() {
        for (TrackLanes lane : getLanes()) {
            if (lane.index == getPreviousPosition().lane.startLaneIndex) {
                return lane;
            }
        }

        return null;
    }

    public TrackLanes isLeftLane() {
        for (TrackLanes lane : this.getLanes()) {
            if (lane.index < this.getMyLane().index) {
                return lane;
            }
        }
        return null;
    }

    public TrackLanes isRightLane() {
        for (TrackLanes lane : this.getLanes()) {
            if (lane.index > this.getMyLane().index) {
                return lane;
            }
        }
        return null;
    }

    public LapResults getLapResults() {
        return lapResults;
    }

    public void clearTurbo() {
        this.turbo = null;
    }

    public boolean isMyTurboOn() {
        return this.myTurboOn;
    }

    public void update(TurboStartMsg message) {
        if (message.getData().isSameCar(this.myCar)) {
            System.out.println("My turbo on!");
            this.myTurboOn = true;
        }
    }

    public void update(TurboEndMsg message) {
        if (message.getData().isSameCar(this.myCar)) {
            System.out.println("My turbo off!");
            this.myTurboOn = false;
        }
    }

    public boolean isOnSameLane(Lane lane) {
        if (lane.endLaneIndex == this.getPreviousPosition().lane.endLaneIndex) {
            return true;
        }
        return false;
    }

    public boolean anyoneAheadAndSameLane() {
        this.getTrackModel().getCurrentIndex();
        for (CarPosition pos : this.opponentPositions) {
            if (!pos.id.isSameCar(this.myCar)) {
                if ((this.getTrackModel().getCurrentIndex() == pos.piecePosition.pieceIndex && this
                        .getPreviousPosition().inPieceDistance < pos.piecePosition.inPieceDistance)
                        || this.getTrackModel().getNextIndex() == pos.piecePosition.pieceIndex) {
                    if (isOnSameLane(pos.piecePosition.lane)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
