package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.model.World;

public class SlowBot implements Driver {

    @Override
    public double getThrottle(World world) {
        return 0.5d;
    }

    @Override
    public String getLane(World world) {
        // TODO Auto-generated method stub
        return null;
    }

}
