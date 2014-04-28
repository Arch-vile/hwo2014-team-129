package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.model.TrackModel;
import hwo.kurjatturskat.model.World;

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

        double curveSpeed = determineMaxCurveSpeed(track.getNextCurve());

        if (curveSpeed > 10)
            curveSpeed = 10;

        this.nextCurve = track.getNextCurve();
        this.speedForNextCurve = curveSpeed;

        Double shouldWeBreak = shouldWeBreak(curveSpeed, world);
        if (shouldWeBreak != null) {
            return shouldWeBreak;
        }

        return null;

    }

    private Double shouldWeBreak(double desiredSpeed, World world) {

        TrackModel track = world.getTrackModel();
        double distance = track.getLaneDistanceBetweenPieces(
                track.getCurrent(), track.getNextCurve(), world.getMyLane());
        double inPieceDistance = world.myPhysics.getPreviousPosition().inPieceDistance;
        double distanceToCurve = distance - inPieceDistance;

        double endSpeed;
        double newThrottle = 0;

        endSpeed = world.getMySpeed();

        int ticksTillCurve = ticksToRunDistance(distanceToCurve, world,
                newThrottle);

        if (ticksTillCurve == -1)
            return null;

        for (int i = 0; i < ticksTillCurve; i++) {
            endSpeed = world.myPhysics.getAccelerationEstimator()
                    .getSpeedOnNextTick(endSpeed, newThrottle);
        }

        // Now we have reached the point that
        if (endSpeed > desiredSpeed) {
            return 0d;
        } else {
            return null;
        }

    }

    private int ticksToRunDistance(double distance, World world, double throttle) {

        double speedAtStartOfTick = world.getMySpeed();

        int ticks = 0;
        while (distance > 0) {
            double speedAtEndOfTick = world.myPhysics
                    .getAccelerationEstimator().getSpeedOnNextTick(
                            speedAtStartOfTick, throttle);
            double distanceToTravelInNextTick = (speedAtEndOfTick + speedAtEndOfTick) / 2;
            if (distanceToTravelInNextTick < 0.0001)
                return -1;
            distance -= distanceToTravelInNextTick;
            ticks++;
            speedAtStartOfTick = speedAtEndOfTick;
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
