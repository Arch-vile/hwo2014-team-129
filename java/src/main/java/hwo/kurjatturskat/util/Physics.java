package hwo.kurjatturskat.util;

import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.model.TrackPosition;

/**
 * Class for keeping and retrieving current physics information.
 * 
 * @author tommy
 */
public class Physics {

    /**
     * Lanes are needed for calculating the distance in previous lane.
     */
    private TrackLanes[] lanes;

    /**
     * Current speed.
     */
    private double speed;

    private double carAngle;

    private double speedDelta;

    private double carAngleDelta;

    private double acceleration;

    /**
     * Throttle set by driver.
     */
    private double throttle;

    public TrackPosition previousPosition;

    /**
     * The track lanes are required so we know the distances from center.
     * TrackUtils need lane information for distance calculation.
     * 
     * @param lanes
     */
    public Physics(TrackLanes[] lanes) {
        this.lanes = lanes;
    }

    public void addTrackPosition(TrackPosition position) {
        // do speed calculation
        if (this.previousPosition == null) {
            this.carAngle = position.carAngle;
            if (position.gameTick == 0) {
                this.speed = 0.0;
            } else {
                this.speed = position.inPieceDistance / position.gameTick;
            }
            this.speedDelta = this.speed;
            this.carAngleDelta = position.carAngle;
        } else {
            TrackLanes lane = this
                    .getLane(this.previousPosition.lane.endLaneIndex);
            double distance = TrackUtils.getDistance(this.previousPosition,
                    position, lane);
            int tickDiff = position.gameTick - this.previousPosition.gameTick;
            double newSpeed = distance / tickDiff;
            this.speedDelta = newSpeed - this.speed;
            this.speed = newSpeed;
            this.acceleration = this.speedDelta / tickDiff;
            double carAngle = position.carAngle;
            this.carAngleDelta = carAngle - this.carAngle;
            this.carAngle = position.carAngle;
        }
        this.previousPosition = position;
    }

    public double getCurrentSpeed() {
        return this.speed;
    }

    public double getCurrentCarAngle() {
        return this.carAngle;
    }

    public double getSpeedDelta() {
        return this.speedDelta;
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public double getCarAngleDelta() {
        return this.carAngleDelta;
    }

    public void setThrottle(double throttle) {
        this.throttle = throttle;
    }

    public double getThrottle() {
        return this.throttle;
    }

    public TrackPosition getPreviousPosition() {
        return this.previousPosition;
    }

    private TrackLanes getLane(int index) {
        for (TrackLanes lane : this.lanes) {
            if (index == lane.index) {
                return lane;
            }
        }
        return null;
    }
}
