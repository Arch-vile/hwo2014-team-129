package hwo.kurjatturskat.ai.behaviours.turbo;

import hwo.kurjatturskat.ai.behaviours.spec.TurboBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SlowToCurvesBehaviour;
import hwo.kurjatturskat.model.TrackModel;
import hwo.kurjatturskat.model.World;
import hwo.kurjatturskat.util.TrackUtils;

public class TurboOnStraightBehaviour implements TurboBehaviour {

    @Override
    public Boolean launchTurbo(World world) {

        TrackModel track = world.getTrackModel();
        double distPiecesToCurve = track.getLaneDistanceBetweenPieces(
                track.getCurrent(), track.getNextCurveStart(),
                world.getMyLane());
        double distOnPiece = TrackUtils.getPieceLenght(track.getCurrent(),
                world.getMyLane())
                - world.myPhysics.getPreviousPosition().inPieceDistance;
        double distToCurve = distPiecesToCurve + distOnPiece;

        double nextCurveMaxSpeed = SlowToCurvesBehaviour
                .determineMaxCurveSpeed(track.getNextCurve())
                * SlowToCurvesBehaviour.ERROR_CORRECTION;
        if (nextCurveMaxSpeed > 10)
            nextCurveMaxSpeed = 10 * SlowToCurvesBehaviour.ERROR_CORRECTION;

        double carAngle = world.getPreviousPosition().carAngle;
        double carAngleSpeed = world.myPhysics.getCarAngleSpeed();

        if (world.getTurbo() != null) {
            if (world.getTrackModel().getCurrent().isCurve()
                    && world.anyoneAheadAndSameLane()) {
                System.out.println("Somene in front, turbo!");
                return true;
            } else if (distToCurve >= 200 && nextCurveMaxSpeed > 5.0
                    && !world.getTrackModel().getCurrent().isCurve()
                    && Math.abs(carAngle) < 5 && Math.abs(carAngleSpeed) < 5) {

                System.out.println("Turbo! Distance to curve: " + distToCurve
                        + ", nextCurveMaxSpeed:" + nextCurveMaxSpeed
                        + ", carAngle: " + carAngle + ",carAngleAcceleration: "
                        + carAngleSpeed);
                return true;
            }
        }
        return null;
    }
}
