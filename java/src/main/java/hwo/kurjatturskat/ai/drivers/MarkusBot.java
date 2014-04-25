package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.ai.behaviours.lane.ShortestPathBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.FastOnStraightBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SlowOnCurvesBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SpeedCalculusVerificatorBehaviour;
import hwo.kurjatturskat.ai.behaviours.turbo.TurboOnStraightBehaviour;

public class MarkusBot extends Driver {

    public MarkusBot() {
        this.addThrottleBehaviour(new SpeedCalculusVerificatorBehaviour());
        this.addThrottleBehaviour(new SlowOnCurvesBehaviour());
        this.addThrottleBehaviour(new FastOnStraightBehaviour());
        this.addLaneBehaviour(new ShortestPathBehaviour());
        this.addTurboBehaviour(new TurboOnStraightBehaviour());
    }
}
