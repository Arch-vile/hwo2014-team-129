package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.model.World;

public class MarkusBot implements Driver {
    private double lastThrottle = -1;

    private void printNextCurveData(World world) {
        TrackPieces start = world.getTrackModel().getNextCurveStart();
        TrackPieces end = world.getTrackModel().getNextCurveEnd();
        TrackPieces biggestAnglePiece = world.getTrackModel().getBiggestAngle(
                start, end);

        if (start.isCurveLeft())
            System.out.println("Next left! Biggest angle on curve: "
                    + biggestAnglePiece.angle);
        else if (start.isCurveRight())
            System.out.println("Next right! Biggest angle on curve: "
                    + biggestAnglePiece.angle);
    }

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

        if (world.getTrackModel().getCurrent().isCurve()) {
            if (lastThrottle != 0.2) {
                System.out.println("Seems like a curve. Lets slow down.");
                printNextCurveData(world);
            }
            lastThrottle = 0.2;
            return 0.2;
        } else {
            if (lastThrottle != 0.8) {
                System.out.println("We are on straight. Step on it!");
                printNextCurveData(world);
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
