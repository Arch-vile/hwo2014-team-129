package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.model.World;

public class ConstantThrottleBehaviour implements ThrottleBehaviour {

    Double throttle = 0d;

    public ConstantThrottleBehaviour(double throttle) {
        this.throttle = throttle;
    }

    @Override
    public Double getThrottle(World world) {
        return this.throttle;
    }

}
