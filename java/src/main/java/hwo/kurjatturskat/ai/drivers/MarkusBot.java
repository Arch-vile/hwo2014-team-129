package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.ai.behaviours.lane.ShortestPathBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.FastOnStraightBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.KeepSpeedBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SlowToCurvesBehaviour;
import hwo.kurjatturskat.ai.behaviours.turbo.TurboOnStraightBehaviour;

public class MarkusBot extends Driver {

    public MarkusBot() {
        this.addThrottleBehaviour(new SlowToCurvesBehaviour());
        this.addThrottleBehaviour(new KeepSpeedBehaviour());
        this.addThrottleBehaviour(new FastOnStraightBehaviour());
        this.addLaneBehaviour(new ShortestPathBehaviour());
        this.addTurboBehaviour(new TurboOnStraightBehaviour());
    }
}
