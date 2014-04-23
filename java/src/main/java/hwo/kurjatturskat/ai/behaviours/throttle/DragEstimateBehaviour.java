package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.model.World;

public class DragEstimateBehaviour implements ThrottleBehaviour {

    // To eliminate off-by-one errors lets keep the throttle values longer then one tick
    private final static long TICKS_TO_KEEP_THROTTLE = 3;

    @Override
    public Double getThrottle(World world) {
        // Start with full throttle
        if (world.myPhysics.getPreviousPosition().gameTick == null
                || world.myPhysics.getPreviousPosition().gameTick < TICKS_TO_KEEP_THROTTLE) {
            return 1d;
        }

        // Then go to zero throttle
        if (world.myPhysics.getPreviousPosition().gameTick < TICKS_TO_KEEP_THROTTLE * 2) {
            return 0d;
        }

        // Pass control to next throttle behaviour
        return null;
    }
}
