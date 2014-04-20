package hwo.kurjatturskat.ai.drivers;

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
        if (world.getTrackModel().getTrackName().equals("Keimola")
                && (world.getPreviousPosition().pieceIndex > 33 || world
                        .getPreviousPosition().pieceIndex < 4)) {
            return 1;
        }

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
                System.out.println("Next switch: " + nSwitch + " " + nnSwitch);
                System.out.println("Lane0: "
                        + world.getTrackModel().getLaneDistanceBetweenPieces(
                                nSwitch, nnSwitch, 0));
                System.out.println("Lane1: "
                        + world.getTrackModel().getLaneDistanceBetweenPieces(
                                nSwitch, nnSwitch, 1));
                System.out.println("Lane2: "
                        + world.getTrackModel().getLaneDistanceBetweenPieces(
                                nSwitch, nnSwitch, 2));
            }
            lastThrottle = 0.2;
            return 0.2;
        } else {
            if (lastThrottle != 1) {
                System.out.println("We are on straight. Step on it!");
                System.out.println("Next switch: " + nSwitch + " " + nnSwitch);
                System.out.println("Lane0: "
                        + world.getTrackModel().getLaneDistanceBetweenPieces(
                                nSwitch, nnSwitch, 0));
                System.out.println("Lane1: "
                        + world.getTrackModel().getLaneDistanceBetweenPieces(
                                nSwitch, nnSwitch, 1));
                System.out.println("Lane2: "
                        + world.getTrackModel().getLaneDistanceBetweenPieces(
                                nSwitch, nnSwitch, 2));

            }
            lastThrottle = 1;
            return 1;
        }

    }

    @Override
    public String getLane(World world) {
        return null;

        // return null;
    }

}
