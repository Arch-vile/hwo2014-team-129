package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.ai.behaviours.spec.LaneBehaviour;
import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.ai.behaviours.spec.TurboBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.DragEstimateBehaviour;
import hwo.kurjatturskat.model.World;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    private List<ThrottleBehaviour> throttleBehaviours;
    private List<LaneBehaviour> laneBehaviours;
    private List<TurboBehaviour> turboBehaviours;
    private DragEstimateBehaviour dragEstimateBehaviour;

    public Driver() {
        this.throttleBehaviours = new ArrayList<>();
        dragEstimateBehaviour = new DragEstimateBehaviour();
        this.throttleBehaviours.add(dragEstimateBehaviour);

        this.laneBehaviours = new ArrayList<>();
        this.turboBehaviours = new ArrayList<>();
    }

    protected void addThrottleBehaviour(ThrottleBehaviour behaviour) {
        this.throttleBehaviours.add(behaviour);
    }

    protected void addLaneBehaviour(LaneBehaviour behaviour) {
        this.laneBehaviours.add(behaviour);
    }

    protected void addTurboBehaviour(TurboBehaviour behaviour) {
        this.turboBehaviours.add(behaviour);
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

    public Boolean launchTurbo(World world) {
        if (world.getTurbo() == null)
            return null;

        for (TurboBehaviour turboChooser : this.turboBehaviours) {
            Boolean turbo = turboChooser.launchTurbo(world);
            if (Boolean.TRUE.equals(turbo))
                return true;
        }
        return null;
    }

    public DragEstimateBehaviour getDragEstimateBehaviour() {
        return this.dragEstimateBehaviour;
    }

}
