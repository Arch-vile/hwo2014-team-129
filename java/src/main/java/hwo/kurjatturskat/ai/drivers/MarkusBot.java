package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.ai.behaviours.lane.ShortestPathBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.FastOnStraightBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SlowOnCurvesBehaviour;

public class MarkusBot extends Driver {

    public MarkusBot() {
        this.addThrottleBehaviour(new SlowOnCurvesBehaviour());
        this.addThrottleBehaviour(new FastOnStraightBehaviour());
        this.addLaneBehaviour(new ShortestPathBehaviour());
    }

}
