package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.model.World;

public class SpeedCalculusVerificatorBehaviour implements ThrottleBehaviour {

    private Double lastSpeed;
    private Double lastThrottle;

    @Override
    public Double getThrottle(World world) {

        double currentSpeed = world.getMySpeed();

        if (lastSpeed != null && lastThrottle != null) {

            double estimatedSpeed = world.myPhysics.getAccelerationEstimator()
                    .getSpeedOnNextTick(lastSpeed, lastThrottle);

            if (world.onTrack()
                    && Math.abs(estimatedSpeed - currentSpeed) > 0.001) {
                System.err
                        .println("Speed calculations mismatch!! Things may act funny!!");
                System.err.println("Expected speed:\t" + currentSpeed);
                System.err.println("Got speed:\t\t" + estimatedSpeed);
                System.err.println("Difference:\t\t"
                        + Math.abs(estimatedSpeed - currentSpeed));
            }

        }

        this.lastSpeed = currentSpeed;
        this.lastThrottle = world.myPhysics.getThrottle();

        return null;
    }

}
