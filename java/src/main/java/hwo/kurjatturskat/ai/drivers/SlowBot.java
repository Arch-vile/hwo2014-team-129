package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.model.World;

public class SlowBot implements Driver {

    private double lastThrottle = -1;

    @Override
    public double getThrottle(World world) {
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
