package hwo.kurjatturskat.ai.behaviours.lane;

import hwo.kurjatturskat.ai.behaviours.spec.LaneBehaviour;
import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
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

        // NOTICE: Only one lane change can be done on switch.
        if (world.isLeftLane() != null) {
            if (world.getTrackModel().getLaneDistanceBetweenPieces(nSwitch,
                    nnSwitch, world.isLeftLane()) < myLength) {
                return "Left";
            }
        } else if (world.isRightLane() != null) {
            if (world.getTrackModel().getLaneDistanceBetweenPieces(nSwitch,
                    nnSwitch, world.isRightLane()) < myLength) {
                return "Right";
            }
        }

        return null;
    }

}
