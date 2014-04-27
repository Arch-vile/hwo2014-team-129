package hwo.kurjatturskat.ai.behaviours.lane;

import hwo.kurjatturskat.ai.behaviours.spec.LaneBehaviour;
import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.model.TrackModel;
import hwo.kurjatturskat.model.World;

public class ShortestPathBehaviour implements LaneBehaviour {

    @Override
    public String getLane(World world) {
        TrackPieces nSwitch = world.getTrackModel().getNextSwitch();
        TrackPieces nnSwitch = world.getTrackModel().getNextSwitchByIndex(
                nSwitch);

        TrackLanes myLane = world.getMyLane();
        double myLength = world.getTrackModel().getLaneDistanceBetweenPieces(
                nSwitch, nnSwitch, myLane);

        // Change to longer lane if turbo on.
        if (world.isMyTurboOn()) {
            TrackModel track = world.getTrackModel();
            double distToSwitch = track.getLaneDistanceBetweenPieces(
                    track.getCurrent(), nSwitch, world.getMyLane());
            double distToCurve = track.getLaneDistanceBetweenPieces(
                    track.getCurrent(), track.getNextCurveStart(),
                    world.getMyLane());

            if (distToSwitch < distToCurve && distToCurve < 200) {
                if (track.getNextCurveStart().isCurveRight()) {
                    if (world.isLeftLane() != null) {
                        return "Left";
                    }
                } else if (track.getNextCurveStart().isCurveLeft()) {
                    if (world.isRightLane() != null) {
                        return "Right";
                    }
                }
            }
        }

        // NOTICE: Only one lane change can be done on switch.
        if (world.isLeftLane() != null) {
            double leftLength = world.getTrackModel()
                    .getLaneDistanceBetweenPieces(nSwitch, nnSwitch,
                            world.isLeftLane());
            if (leftLength < myLength) {
                return "Left";
            } else if (leftLength == myLength
                    && world.getTrackModel().getNextCurveStart().isCurveLeft()) {
                return "Left";
            }
        }
        if (world.isRightLane() != null) {
            double rightLength = world.getTrackModel()
                    .getLaneDistanceBetweenPieces(nSwitch, nnSwitch,
                            world.isRightLane());
            if (rightLength < myLength) {
                return "Right";
            } else if (rightLength == myLength
                    && world.getTrackModel().getNextCurveStart().isCurveRight()) {
                return "Right";
            }
        }
        return null;
    }
}
