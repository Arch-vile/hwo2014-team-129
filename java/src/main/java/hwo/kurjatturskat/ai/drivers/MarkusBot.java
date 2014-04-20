package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.model.World;

public class MarkusBot implements Driver {
    private double lastThrottle = -1;

    @Override
    public double getThrottle(World world) {
        /*
         * Config to get absolute max speed: keimola When the straight start
         * speed up and store the max speed.
         * 
         * New recordSpeed: 9.483289941799981
         */
        // if (world.getTrackModel().getTrackName().equals("Keimola")
        // && (world.getPreviousPosition().pieceIndex > 33 || world
        // .getPreviousPosition().pieceIndex < 4)) {
        // return 1;
        // }

        /*
         * Stop the car from some speed.. get the distance.
         */
        // if (world.getTrackModel().getTrackName().equals("Keimola")) {
        // if ((world.getMyRaceTime().getCurrentLap() == 0 && world
        // .getPreviousPosition().pieceIndex > 37)
        // || (world.getMyRaceTime().getCurrentLap() == 1 && world
        // .getPreviousPosition().pieceIndex < 2)) {
        // System.out.println("Stop at speed: " + world.getMySpeed());
        // return 0;
        // }
        // }
        TrackPieces nSwitch = world.getTrackModel().getNextSwitch();
        TrackPieces nnSwitch = world.getTrackModel().getNextSwitchByIndex(
                nSwitch);

        if (world.getTrackModel().getCurrent().isCurve()) {
            if (lastThrottle != 0.2) {
                System.out.println("Seems like a curve. Lets slow down.");
                System.out.println("Lane: " + world.getMyLane().index
                        + " Next switch: " + nSwitch + " " + nnSwitch);

                for (int i = 0; i < world.getLanes().length; i++) {
                    TrackLanes lane = world.getLanes()[i];
                    System.out.println("Lane"
                            + i
                            + ": "
                            + world.getTrackModel()
                                    .getLaneDistanceBetweenPieces(nSwitch,
                                            nnSwitch, lane));
                }
            }
            lastThrottle = 0.2;
            return 0.2;
        } else {
            if (lastThrottle != 0.8) {
                System.out.println("We are on straight. Step on it!");
                System.out.println("Lane: " + world.getMyLane().index
                        + " Next switch: " + nSwitch + " " + nnSwitch);
                for (int i = 0; i < world.getLanes().length; i++) {
                    TrackLanes lane = world.getLanes()[i];
                    System.out.println("Lane"
                            + i
                            + ": "
                            + world.getTrackModel()
                                    .getLaneDistanceBetweenPieces(nSwitch,
                                            nnSwitch, lane));
                }
            }
            lastThrottle = 0.8;
            return 0.8;
        }

    }

    @Override
    public String getLane(World world) {
        TrackPieces nSwitch = world.getTrackModel().getNextSwitch();
        TrackPieces nnSwitch = world.getTrackModel().getNextSwitchByIndex(
                nSwitch);

        TrackLanes myLane = world.getMyLane();
        double myLength = world.getTrackModel().getLaneDistanceBetweenPieces(
                nSwitch, nnSwitch, myLane);
        double leftLength = 0;
        double rightLength = 0;
        int drive = 0;

        // TODO: There can be more then two lanes on trac -> more then one lane
        // on left
        if (world.isLeftLane() != null) {
            leftLength = world.getTrackModel().getLaneDistanceBetweenPieces(
                    nSwitch, nnSwitch, world.isLeftLane());
        }
        if (world.isRightLane() != null) {
            rightLength = world.getTrackModel().getLaneDistanceBetweenPieces(
                    nSwitch, nnSwitch, world.isRightLane());
        }
        if (world.isLeftLane() != null && world.isRightLane() != null) {
            if (leftLength < myLength) {
                drive = -1;
            }

            if (rightLength < leftLength) {
                drive = 1;
            }

            if (drive > 0) {
                return "Right";
            } else if (drive < 0) {
                return "Left";
            }

        } else if (world.isLeftLane() != null) {
            if (leftLength < myLength)
                return "Left";
        } else if (world.isRightLane() != null) {
            if (rightLength < myLength)
                return "Right";
        }
        return null;
    }
}
