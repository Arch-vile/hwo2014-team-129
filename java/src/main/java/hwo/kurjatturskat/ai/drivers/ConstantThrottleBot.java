package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.model.World;

public class ConstantThrottleBot implements Driver {

    private double throttle = -1;

    public ConstantThrottleBot(double throttle) {
        this.throttle = throttle;
    }

    @Override
    public double getThrottle(World world) {
        return this.throttle;
    }

    @Override
    public String getLane(World world) {
        return null;
    }

}
