package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.model.World;

public class KeepSpeedBehaviour implements ThrottleBehaviour {

    @Override
    public Double getThrottle(World world) {

        double targetSpeed = targetSpeed(world);

        if (world.getMySpeed() > targetSpeed) {
            return 0d;
        } else {
            return 1d;
        }

    }

    private double targetSpeed(World world) {
        TrackPieces current = world.getTrackModel().getCurrent();
        if (current.isCurve()) {
            return SlowToCurvesBehaviour.determineMaxCurveSpeed(current);
        } else {
            return 10d;
        }
    }

}
