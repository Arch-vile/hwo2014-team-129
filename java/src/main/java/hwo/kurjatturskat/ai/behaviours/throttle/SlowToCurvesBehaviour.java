package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.model.TrackModel;
import hwo.kurjatturskat.model.World;
import hwo.kurjatturskat.util.TrackUtils;

public class SlowToCurvesBehaviour implements ThrottleBehaviour {

    private final static double CURVE_SPEED = 5;

    TrackPieces nextCurve;
    double speedForNextCurve = -1;

    @Override
    public Double getThrottle(World world) {

        TrackModel track = world.getTrackModel();
        if (world.getMySpeed() == 0) {
            return 1.0d;
        }

        if (track.getCurrent().equals(nextCurve)) {
            System.out.println(String.format(
                    "Wanted speed to curve %s but was %s", speedForNextCurve,
                    world.getMySpeed()));
            nextCurve = null;
        }

        if (track.getNext().isCurve()) {

            double curveSpeed = determineMaxCurveSpeed(track.getNext());

            if (curveSpeed > 10)
                curveSpeed = 10;

            this.nextCurve = track.getNext();
            this.speedForNextCurve = curveSpeed;

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

        double endSpeed;
        double newThrottle = 0;
        double lastError = 50;
        while (true) {

            endSpeed = world.getMySpeed();

            int ticksTillCurve = ticksToRunDistance(distanceToCurve, world,
                    newThrottle);
            for (int i = 0; i < ticksTillCurve; i++) {
                endSpeed = world.myPhysics.getAccelerationEstimator()
                        .getSpeedOnNextTick(endSpeed, newThrottle);
            }

            double error = Math.abs(endSpeed - desiredSpeed);
            if (error < lastError) {
                lastError = error;
            } else {
                break;
            }

            newThrottle += 0.1;
        }

        if (newThrottle <= world.myPhysics.getThrottle()) {
            return newThrottle;
        } else {
            return null;
        }
    }

    private int ticksToRunDistance(double distance, World world, double throttle) {

        int ticks = 0;
        while (distance > 0) {

            double speedAtStartOfTick = world.getMySpeed();
            double speedAtEndOfTick = world.myPhysics
                    .getAccelerationEstimator().getSpeedOnNextTick(
                            speedAtStartOfTick, throttle);
            double distanceToTravelInNextTick = (speedAtEndOfTick + speedAtEndOfTick) / 2;
            distance -= distanceToTravelInNextTick;
            ticks++;
        }

        return ticks;

    }

    public static double determineMaxCurveSpeed(TrackPieces next) {
        double curveDiffuculty = determineCurveDifficultu(next);
        return CURVE_SPEED * curveDiffuculty;
    }

    private static double determineCurveDifficultu(TrackPieces next) {
        return next.radius / 200;
    }
}
