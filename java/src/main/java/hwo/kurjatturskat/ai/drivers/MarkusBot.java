package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
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
        int nSwitch = world.getTrackModel().getNextSwitch();
        int nnSwitch = world.getTrackModel().getNextSwitchByIndex(nSwitch);

        if (world.getTrackModel().getCurrent().isCurve()) {
            if (lastThrottle != 0.2) {
                System.out.println("Seems like a curve. Lets slow down.");
                System.out.println("Lane: " + world.getMyLane().index
                        + " Next switch: " + nSwitch + " " + nnSwitch);

                for (int i = 0; i < world.getLanes().length; i++) {
                    System.out.println("Lane"
                            + i
                            + ": "
                            + world.getTrackModel()
                                    .getLaneDistanceBetweenPieces(nSwitch,
                                            nnSwitch, i));
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
                    System.out.println("Lane"
                            + i
                            + ": "
                            + world.getTrackModel()
                                    .getLaneDistanceBetweenPieces(nSwitch,
                                            nnSwitch, i));
                }
            }
            lastThrottle = 0.8;
            return 0.8;
        }

    }

    @Override
    public String getLane(World world) {
        int nSwitch = world.getTrackModel().getNextSwitch();
        int nnSwitch = world.getTrackModel().getNextSwitchByIndex(nSwitch);

        TrackLanes myLane = world.getMyLane();
        double myLength = world.getTrackModel().getLaneDistanceBetweenPieces(
                nSwitch, nnSwitch, myLane.index);
        double leftLength = 0;
        double rightLength = 0;
        int drive = 0;

        if (world.isLeftLane()) {
            leftLength = world.getTrackModel().getLaneDistanceBetweenPieces(
                    nSwitch, nnSwitch, myLane.index - 1);
        }
        if (world.isRightLane()) {
            rightLength = world.getTrackModel().getLaneDistanceBetweenPieces(
                    nSwitch, nnSwitch, myLane.index + 1);
        }
        if (world.isLeftLane() && world.isRightLane()) {
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

        } else if (world.isLeftLane()) {
            if (leftLength < myLength)
                return "Left";
        } else if (world.isRightLane()) {
            if (rightLength < myLength)
                return "Right";
        }
        return null;
    }
}
