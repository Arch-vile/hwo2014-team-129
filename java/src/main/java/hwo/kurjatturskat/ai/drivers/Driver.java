package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.model.World;

public interface Driver {

    public double getThrottle(World world);

    public String getLane(World world);

}
