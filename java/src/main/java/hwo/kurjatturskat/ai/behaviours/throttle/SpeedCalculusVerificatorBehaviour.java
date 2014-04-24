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
                    && Math.abs(estimatedSpeed - currentSpeed) > 0.00001) {
                System.err
                        .println("Speed calculations mismatch!! Nothing wont work!!");
                System.err.println("Expected:\t" + currentSpeed);
                System.err.println("Got:\t\t" + estimatedSpeed);
            }

        }

        this.lastSpeed = currentSpeed;
        this.lastThrottle = world.myPhysics.getThrottle();

        return null;
    }

}
