package hwo.kurjatturskat.util;

import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.model.TrackPosition;

import org.la4j.LinearAlgebra;
import org.la4j.linear.LinearSystemSolver;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CRSMatrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

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
     * Needed for trigonometry and stuff.
     * 
     * Defaults due to GameInit cars part parsing not working
     */
    private double carLength = 40.0;
    private double flagPosition = 10.0;

    /**
     * Current speed.
     */
    private double speed;
    private double speedDelta;
    private double acceleration;

    private double carAngle;
    private double carAngleDelta;
    private double carAngleSpeed;
    private double carAngleAcceleration;

    /**
     * Throttle set by driver.
     */
    private double throttle;

    /**
     * Throttle seems to be delayed.
     */
    private double prevThrottle;

    public TrackPosition previousPosition;

    /**
     * What is the approximated rolling friction coefficient.
     */
    private double approxRollingFriction;

    /**
     * What is the approximated drag coefficient.
     */
    private double approxDrag;

    private double approxCarAngleFriction;

    private int approxCarAngleFrictionCount = 0;

    /**
     * How many datasets have we in our approximated drag and rolling friction.
     */
    private int dataSetCount = 0;

    /**
     * This is used to adjust the approximated drag and rolling friction
     * coefficients to nice values.
     */
    private double enginePower = 1;

    /**
     * Here we store the previous steps (v(t))^2, -v(t) and a(t)-T(t) values.
     * index 0 = (v(t))^2 1 = -v(t) 2 = a(t) - T(t) These are then used to
     * populate parts in the CRS and B vector.
     */
    private double[] previousCoeffs = new double[3];

    /**
     * The track lanes are required so we know the distances from center.
     * TrackUtils need lane information for distance calculation.
     * 
     * @param lanes
     */
    public Physics(TrackLanes[] lanes) {
        this.lanes = lanes;
    }

    public void setCarDimensions(double carLength, double flagPosition) {
        this.carLength = carLength;
        this.flagPosition = flagPosition;
    }

    public void addTrackPosition(TrackPosition position) {
        // do speed calculation
        if (this.previousPosition == null) {
            this.carAngle = position.carAngle;
            if (position.gameTick == 0) {
                this.speed = 0.0;
                this.carAngleSpeed = 0.0;
            } else {
                this.speed = position.inPieceDistance / position.gameTick;
            }
            this.speedDelta = this.speed;
            this.carAngleDelta = this.carAngle;
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
            this.carAngleSpeed = this.carAngleDelta / tickDiff;
            this.carAngleAcceleration = this.carAngleSpeed / tickDiff;
            this.carAngle = carAngle;

            double carAngleFriction = this.calculateCarAngleFriction(
                    this.carAngleSpeed, this.speed, this.carAngle,
                    this.carAngleAcceleration);

            if (carAngleFriction != 0.0) {
                // add to our averaging
                this.addNewestCarAngleFriction(carAngleFriction);
            }

            // should we have previous throttle?
            double[] newCoeffs = { Math.pow(newSpeed, 2) / 2, -1 * newSpeed,
                    this.acceleration - this.prevThrottle };
            if (this.previousCoeffs == null) {
                this.previousCoeffs = newCoeffs;
            } else { // solve the equations
                // Let's do matrix calculation
                // we can only do this if our speed is non-zero
                if (newSpeed != 0) {
                    Vector solutions = this.approximateDragAndFriction(
                            newCoeffs, this.previousCoeffs);
                    if (solutions != null) {
                        this.addNewestSolutionToApproximation(solutions);
                    }
                }
                this.previousCoeffs = newCoeffs;
            }

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

    public double getCarAngleAcceleration() {
        return this.carAngleAcceleration;
    }

    public void setThrottle(double throttle) {
        this.prevThrottle = throttle;
        this.throttle = throttle;
    }

    public double getThrottle() {
        return this.throttle;
    }

    public TrackPosition getPreviousPosition() {
        return this.previousPosition;
    }

    public double getApproximateDrag() {
        return this.approxDrag;
    }

    public double getApproximateRollingFriction() {
        return this.approxRollingFriction;
    }

    public double getApproxCarAngleFriction() {
        return this.approxCarAngleFriction;
    }

    private TrackLanes getLane(int index) {
        for (TrackLanes lane : this.lanes) {
            if (index == lane.index) {
                return lane;
            }
        }
        return null;
    }

    private double getAngleAsRadians(double angle) {
        return angle / 360 * 2 * Math.PI;
    }

    /**
     * We calculate car angle friction for given values.
     * 
     * It slows down the angle acceleration from what would be expected without
     * friction.
     * 
     * @param angleSpeed
     * @param carSpeed
     * @param angle
     * @param angleAcceleration
     * @return
     */
    private double calculateCarAngleFriction(double angleSpeed,
            double carSpeed, double angle, double angleAcceleration) {
        // if we have no valid values for friction calculation we abort
        if (angle == 0 && angleSpeed == 0 && angleAcceleration == 0) {
            // abort, we have no information to calculate from
            return 0.0;
        }

        System.out.println("Anglestuff: " + angleSpeed + " " + carSpeed + " "
                + angle + " " + angleAcceleration);

        // All to radians
        // required as positive for trigs
        double radianAngle = this.getAngleAsRadians(Math.abs(angle));

        double radianAngleAcc = Math.abs(angleAcceleration) / 360 * 2 * Math.PI;
        double radianAngleSpeed = this.getAngleAsRadians(Math.abs(angleSpeed));

        double frictionConstant = radianAngleSpeed
                - ((carSpeed * Math.sin(radianAngle)) / (this.carLength - this.flagPosition))
                - radianAngleAcc;

        return frictionConstant;
    }

    /**
     * This function approximates drag and rolling friction given coefficients
     * for linear equation solving.
     * 
     * The arrays contain index 0 = (v(t))^2, 1 = -v(t), 2 = a(t) - T(t)
     * 
     * @param current
     *            Contains the current values.
     * @param previous
     *            Contains the previous values.
     */
    private Vector approximateDragAndFriction(double[] current,
            double[] previous) {
        /*
         * // The coefficient matrix 'a' is a CRS (Compressed Row Storage)
         * matrix Matrix a = new CRSMatrix(new double[][] { { 1.0, 2.0, 3.0 }, {
         * 4.0, 5.0, 6.0 }, { 7.0, 8.0. 9.0 } });
         * 
         * // A right hand side vector, which is simple dense vector Vector b =
         * new BasicVector(new double[] { 1.0, 2.0, 3.0 });
         * 
         * // We will use standard Forward-Back Substitution method, // which is
         * based on LU decomposition and can be used with square systems
         * LinearSystemSolver solver =
         * a.withSolver(LinearAlgebra.FORWARD_BACK_SUBSTITUTION); // The 'x'
         * vector will be sparse Vector x = solver.solve(b,
         * LinearAlgebra.SPARSE_FACTORY);
         */
        // Matrix a = new Basic1DMatrix(new double[][] {

        Matrix a = new CRSMatrix(new double[][] { { current[0], current[1] },
                { previous[0], current[1] } });
        Vector b = new BasicVector(new double[] { current[2], previous[2] });

        // System.out.println("CURRENT: " + current[0] + " " + current[1] + " "
        // + current[2]);
        // System.out.println("PREVIOUS: " + previous[0] + " " + previous[1] +
        // " "
        // + previous[2]);
        // System.out.flush();
        // System.out.println(b);
        // LinearSystemSolver solver =
        // a.withSolver(LinearAlgebra.LEAST_SQUARES);
        LinearSystemSolver solver = a
                .withSolver(LinearAlgebra.FORWARD_BACK_SUBSTITUTION);
        // LinearSystemSolver solver =
        // a.withSolver(LinearAlgebra.LEAST_SQUARES);

        try {
            Vector x = solver.solve(b, LinearAlgebra.SPARSE_FACTORY);
            return x;
        } catch (IllegalArgumentException e) {
            // probably matrix values to close or such, whatever
            return null;
        }
    }

    private void addNewestCarAngleFriction(double carAngleFriction) {
        if (this.dataSetCount == 0) {
            this.approxCarAngleFriction = carAngleFriction;
        } else {
            int newCount = this.approxCarAngleFrictionCount + 1;
            this.approxCarAngleFriction = (this.approxCarAngleFriction
                    * this.approxCarAngleFrictionCount + carAngleFriction)
                    / newCount;
        }
        this.approxCarAngleFrictionCount++;
    }

    protected void addNewestSolutionToApproximation(Vector x) {
        // we average the drag solutions, should we discard impossible values?
        // c1, i.e. drag, should be negative
        // c2, i.e. rolling friction should be positive
        double newDrag = x.get(0);
        double newRollFriction = x.get(1);
        if (this.dataSetCount == 0) {
            this.approxDrag = newDrag;
            this.approxRollingFriction = newRollFriction;
        } else {
            int newCount = this.dataSetCount + 1;
            this.approxDrag = (this.approxDrag * this.dataSetCount + newDrag)
                    / newCount;
            this.approxRollingFriction = (this.approxRollingFriction
                    * this.dataSetCount + newRollFriction)
                    / newCount;
        }
        this.dataSetCount++;
    }

}
