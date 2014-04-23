package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.ai.behaviours.spec.LaneBehaviour;
import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.model.World;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    private List<ThrottleBehaviour> throttleBehaviours;
    private List<LaneBehaviour> laneBehaviours;

    public Driver() {
        this.throttleBehaviours = new ArrayList<>();
        this.laneBehaviours = new ArrayList<>();
    }

    protected void addThrottleBehaviour(ThrottleBehaviour behaviour) {
        this.throttleBehaviours.add(behaviour);
    }

    protected void addLaneBehaviour(LaneBehaviour behaviour) {
        this.laneBehaviours.add(behaviour);
    }

    public String getLane(World world) {
        for (LaneBehaviour laneChooser : this.laneBehaviours) {
            String lane = laneChooser.getLane(world);
            if (lane != null) {
                return lane;
            }
        }
        return null;
    }

    public Double getThrottle(World world) {
        for (ThrottleBehaviour throttleChooser : this.throttleBehaviours) {
            Double throttle = throttleChooser.getThrottle(world);
            if (throttle != null)
                return throttle;
        }
        return null;
    }

}
