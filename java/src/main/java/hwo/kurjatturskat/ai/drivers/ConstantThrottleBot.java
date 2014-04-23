package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.ai.behaviours.throttle.ConstantThrottleBehaviour;

public class ConstantThrottleBot extends Driver {

    public ConstantThrottleBot(double throttle) {
        this.addThrottleBehaviour(new ConstantThrottleBehaviour(0.5));
    }

}
