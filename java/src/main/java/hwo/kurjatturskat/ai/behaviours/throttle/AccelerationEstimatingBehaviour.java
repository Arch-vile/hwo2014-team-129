package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.model.World;
import hwo.kurjatturskat.util.AccelerationEstimator;
import hwo.kurjatturskat.util.DragEstimator;

import java.util.ArrayList;
import java.util.List;

public class AccelerationEstimatingBehaviour implements ThrottleBehaviour {

    // To eliminate off-by-one errors and other strange behaviour related to
    // changing throttle keep enough points
    private final static int TICKS_TO_KEEP_THROTTLE = 5;

    private final List<Double> speedPerTickOnFullThrottle = new ArrayList<>();
    private final List<Double> speedPerTickOnZeroThrottle = new ArrayList<>();

    private boolean samplesReady = false;

    private AccelerationEstimator accelerationEstimator;
    private DragEstimator dragEstimator;

    public AccelerationEstimatingBehaviour() {
        this.dragEstimator = new DragEstimator();
        this.accelerationEstimator = new AccelerationEstimator(
                this.dragEstimator);
    }

    // TODO: make sure we are not doing this on actualy race. only on the time
    // rounds
    @Override
    public Double getThrottle(World world) {
        // Start with full throttle
        if (world.myPhysics.getPreviousPosition() == null
                || world.myPhysics.getPreviousPosition().gameTick == null) {
            return 1d;
        }

        // Start with full throttle
        if (world.myPhysics.getPreviousPosition().gameTick < TICKS_TO_KEEP_THROTTLE) {
            this.speedPerTickOnFullThrottle.add(world.getMySpeed());
            return 1d;
        }

        // Then go to zero throttle
        if (world.myPhysics.getPreviousPosition().gameTick < TICKS_TO_KEEP_THROTTLE * 2) {
            this.speedPerTickOnZeroThrottle.add(world.getMySpeed());
            return 0d;
        }

        // Ready to estimate
        if (this.dragEstimator.getD() == null
                || this.accelerationEstimator.getA() == null) {
            estimate();
        }

        // Pass control to next throttle behaviour
        samplesReady = true;
        return null;
    }

    private void estimate() {

        this.dragEstimator
                .estimateDragConstant(getSpeedSamplesOnZeroThrottle());

        this.accelerationEstimator
                .estimateAccelerationConstant(getSpeedSamplesOnFullThrottle());

    }

    public boolean samplesReady() {
        return samplesReady;
    }

    private double[] getSpeedSamplesOnZeroThrottle() {
        double[] samplesOnZeroThrottle = new double[] {
                this.speedPerTickOnZeroThrottle.get(1),
                this.speedPerTickOnZeroThrottle.get(2) };
        return samplesOnZeroThrottle;
    }

    private double[] getSpeedSamplesOnFullThrottle() {
        double[] samplesOnFullThrottle = new double[] {
                this.speedPerTickOnFullThrottle.get(1),
                this.speedPerTickOnFullThrottle.get(2) };
        return samplesOnFullThrottle;
    }

    public AccelerationEstimator getAccelerationEstimator() {
        return this.accelerationEstimator;
    }

}
