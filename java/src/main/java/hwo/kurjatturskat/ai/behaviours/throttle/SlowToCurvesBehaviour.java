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

        System.out.println(world.getMySpeed());
        if (track.getCurrent().isCurve()) {
            System.exit(1);
        }

        if (track.getNext().isCurve()) {
            double curveSpeed = determineMaxCurveSpeed(track.getNext());
            if (curveSpeed > 10)
                curveSpeed = 10;

            Double shouldWeBreak = shouldWeBreak(curveSpeed, world);
            if (shouldWeBreak != null) {
                return shouldWeBreak;
            }
        }

        return null;

    }

    private Double shouldWeBreak(double desiredSpeed, World world) {
        TrackModel track = world.getTrackModel();
        double currentPieceLength = TrackUtils.getPieceLenght(
                track.getCurrent(), world.getMyLane());
        double inPieceDistance = world.myPhysics.getPreviousPosition().inPieceDistance;
        double distanceToCurve = currentPieceLength - inPieceDistance;

        int ticksTillCurve = ticksToRunDistance(distanceToCurve, world);

        double newThrottle = 0;
        double endSpeedError = 10;
        double lastEndSpeedError = 1;
        while (endSpeedError < lastEndSpeedError) {
            lastEndSpeedError = endSpeedError;

            double endSpeed = world.getMySpeed();
            for (int i = 0; i < ticksTillCurve; i++) {
                endSpeed = world.myPhysics.getAccelerationEstimator()
                        .getSpeedOnNextTick(endSpeed, newThrottle);
            }

            endSpeedError = Math.abs(endSpeed - desiredSpeed);
            newThrottle += 0.1;
        }

        if (newThrottle < world.myPhysics.getThrottle()) {
            return newThrottle;
        } else {
            return null;
        }
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
