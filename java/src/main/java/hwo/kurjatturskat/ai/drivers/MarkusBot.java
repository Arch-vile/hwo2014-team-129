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
        if (world.getTrackModel().getTrackName().equals("keimola")
                && (world.getPreviousPosition().pieceIndex > 33 || world
                        .getPreviousPosition().pieceIndex < 4)) {
            System.out.println("Max speed!!!");
            return 1;
        }

        if (world.getTrackModel().getCurrent().isCurve()) {
            if (lastThrottle != 0.2) {
                System.out.println("Seems like a curve. Lets slow down.");
            }
            lastThrottle = 0.2;
            return 0.2;
        } else {
            if (lastThrottle != 1) {
                System.out.println("We are on straight. Step on it!");
            }
            lastThrottle = 1;
            return 1;
        }

    }

    @Override
    public String getLane(World world) {
        // TODO Auto-generated method stub
        return null;
    }

}
