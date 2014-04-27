package hwo.kurjatturskat.ai.behaviours.turbo;

import hwo.kurjatturskat.ai.behaviours.spec.TurboBehaviour;
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

        if (world.getTurbo() != null) {
            if (world.getTrackModel().getCurrent().isCurve()
                    && world.anyoneAheadAndSameLane()) {
                System.out.println("Somene in front, turbo!");
                return true;
            } else if (distToCurve >= 200
                    && !world.getTrackModel().getCurrent().isCurve()) {
                System.out.println("Turbo! Distance to curve: " + distToCurve);
                return true;
            }
        }
        return null;
    }
}
