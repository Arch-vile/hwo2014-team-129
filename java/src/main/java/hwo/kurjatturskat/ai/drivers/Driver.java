package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.ai.behaviours.spec.LaneBehaviour;
import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.ai.behaviours.spec.TurboBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SlipConstantEstimatorBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SpeedSampleCollectorBehaviour;
import hwo.kurjatturskat.model.World;
import hwo.kurjatturskat.util.AccelerationEstimator;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    private List<ThrottleBehaviour> throttleBehaviours;
    private List<LaneBehaviour> laneBehaviours;
    private List<TurboBehaviour> turboBehaviours;
    private SpeedSampleCollectorBehaviour dragEstimateBehaviour;

    // TODO: remove tweak code. fix when below todo also done.
    private boolean set = false;

    // TODO: move all the estimators to be part of the behavious like the slip estimator
    public Driver() {
        this.throttleBehaviours = new ArrayList<>();
        dragEstimateBehaviour = new SpeedSampleCollectorBehaviour();
        this.throttleBehaviours.add(dragEstimateBehaviour);

        this.laneBehaviours = new ArrayList<>();
        this.turboBehaviours = new ArrayList<>();
    }

    public void setSlipEstimatingBehaviour(AccelerationEstimator accEstimator) {
        this.throttleBehaviours.add(0, new SlipConstantEstimatorBehaviour(
                accEstimator));
        this.set = true;
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

    public SpeedSampleCollectorBehaviour getDragEstimateBehaviour() {
        return this.dragEstimateBehaviour;
    }

    public boolean slipEstimatorSet() {
        return set;
    }

}
