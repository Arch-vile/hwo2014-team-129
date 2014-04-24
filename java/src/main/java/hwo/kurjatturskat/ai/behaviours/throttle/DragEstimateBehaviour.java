package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.model.World;

import java.util.ArrayList;
import java.util.List;

public class DragEstimateBehaviour implements ThrottleBehaviour {

    // To eliminate off-by-one errors and other strange behaviour related to changing throttle keep enough points
    private final static int TICKS_TO_KEEP_THROTTLE = 5;

    private final List<Double> speedPerTickOnFullThrottle = new ArrayList<>();
    private final List<Double> speedPerTickOnZeroThrottle = new ArrayList<>();

    private boolean samplesReady = false;

    // TODO: make sure we are not doing this on actualy race. only on the time rounds
    @Override
    public Double getThrottle(World world) {
        // Start with full throttle
        if (world.myPhysics.getPreviousPosition().gameTick == null) {
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

        // Pass control to next throttle behaviour
        samplesReady = true;
        return null;
    }

    public boolean samplesReady() {
        return samplesReady;
    }

    public double[] getSpeedSamplesOnZeroThrottle() {
        double[] samplesOnZeroThrottle = new double[] {
                this.speedPerTickOnZeroThrottle.get(1),
                this.speedPerTickOnZeroThrottle.get(2) };
        return samplesOnZeroThrottle;
    }

    public List<Double> getRecorededValuesOnZeroThrottle() {
        return this.speedPerTickOnZeroThrottle;
    }
}
