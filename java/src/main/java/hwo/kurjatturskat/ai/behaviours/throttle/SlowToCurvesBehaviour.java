package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.model.TrackModel;
import hwo.kurjatturskat.model.World;
import hwo.kurjatturskat.util.TrackUtils;

public class SlowToCurvesBehaviour implements ThrottleBehaviour {

    private final static double CURVE_SPEED = 5;

    @Override
    public Double getThrottle(World world) {
        TrackModel track = world.getTrackModel();

        if (track.getNext().isCurve()) {
            double curveSpeed = determineMaxCurveSpeed(track.getNext());
            if (curveSpeed > 10)
                curveSpeed = 10;

            boolean shouldWeBreak = shouldWeBreak(curveSpeed, world);
            if (shouldWeBreak) {
                return 0d;
            }
        }

        return null;

    }

    private boolean shouldWeBreak(double desiredSpeed, World world) {
        TrackModel track = world.getTrackModel();
        double currentPieceLength = TrackUtils.getPieceLenght(
                track.getCurrent(), world.getMyLane());
        double inPieceDistance = world.myPhysics.getPreviousPosition().inPieceDistance;
        double distanceToCurve = currentPieceLength - inPieceDistance;

        int ticksTillCurve = ticksToRunDistance(distanceToCurve, world);

        double endSpeed = world.getMySpeed();
        for (int i = 0; i < ticksTillCurve - 100; i++) {
            endSpeed = world.myPhysics.getAccelerationEstimator()
                    .getSpeedOnNextTick(endSpeed, 0);
        }

        if (endSpeed > desiredSpeed) {
            return true;
        }

        return false;
    }

    private int ticksToRunDistance(double distance, World world) {

        int ticks = 0;
        while (distance > 0) {

            double speedAtStartOfTick = world.getMySpeed();
            double speedAtEndOfTick = world.myPhysics
                    .getAccelerationEstimator().getSpeedOnNextTick(
                            speedAtStartOfTick, world.myPhysics.getThrottle());
            double distanceToTravelInNextTick = (speedAtEndOfTick + speedAtEndOfTick) / 2;
            distance -= distanceToTravelInNextTick;
            ticks++;
        }

        return ticks;

    }

    public static double determineMaxCurveSpeed(TrackPieces next) {
        double curveDiffuculty = determineCurveDifficultu(next);
        return CURVE_SPEED / curveDiffuculty;
    }

    private static double determineCurveDifficultu(TrackPieces next) {
        return (100 / next.radius);
    }
}
