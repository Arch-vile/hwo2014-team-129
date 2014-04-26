package hwo.kurjatturskat.ai.behaviours.throttle;

import hwo.kurjatturskat.ai.behaviours.spec.ThrottleBehaviour;
import hwo.kurjatturskat.model.World;
import hwo.kurjatturskat.util.AccelerationEstimator;
import hwo.kurjatturskat.util.SlipEstimator;

import java.util.ArrayList;
import java.util.List;

public class SlipEstimatingBehaviour implements ThrottleBehaviour {

    private final int SAMPLE_COUNT = 3;

    private List<DataSample> samples = new ArrayList<>();
    private SlipEstimator slipEstimator;
    private DataSample lastSample;

    public SlipEstimatingBehaviour(AccelerationEstimator accelerationEstimator) {
        this.slipEstimator = new SlipEstimator(accelerationEstimator);
    }

    @Override
    public Double getThrottle(World world) {

        if (world.getTrackModel().getCurrent().isCurve()) {

            DataSample current = new DataSample(
                    world.myPhysics.getCurrentCarAngle(),
                    world.myPhysics.getCurrentSpeed(), world.getTrackModel()
                            .getCurrent().radius,
                    world.myPhysics.getThrottle(),
                    world.myPhysics.getCarLength() / 2
                            - world.myPhysics.getFlagPosition());

            if (this.samples.size() < SAMPLE_COUNT) {
                this.samples.add(current);
                assertOnSameCurve();
            }

            if (this.samples.size() >= SAMPLE_COUNT
                    && this.slipEstimator.getSlipConstant() == null) {
                this.slipEstimator.estimateSlipConstant(
                        this.samples.get(SAMPLE_COUNT - 2),
                        this.samples.get(SAMPLE_COUNT - 1));
            }

            if (this.slipEstimator.getS() != null && lastSample != null) {
                double estimate = this.slipEstimator
                        .estimateSlipAngleOnNextTick(lastSample.slipAngle,
                                lastSample.carSpeed, lastSample.curveRadius,
                                lastSample.throttle, lastSample.centerDistance);

                if (Math.abs(current.slipAngle - estimate) > 0.0001) {
                    System.err
                            .println("Slip estimate seems off! You are on thin ice now!");
                    System.err.println("\nExpected slip angle:\t"
                            + current.slipAngle);
                    System.err.println("Actual slip angle\t\t:" + estimate);
                    System.err.println("Difference:\t"
                            + (current.slipAngle - estimate));
                    System.out.println(current);
                }
            }

            this.lastSample = current;
        } else {
            this.lastSample = null;
        }

        return null;
    }

    private void assertOnSameCurve() {
        if (samples.size() >= SAMPLE_COUNT) {
            double radius = samples.get(0).curveRadius;
            for (DataSample sample : this.samples) {
                if (sample.curveRadius != radius) {
                    this.samples.clear();
                }
            }
        }
    }

    public SlipEstimator getSlipEstimator() {
        return slipEstimator;
    }

    public static class DataSample {
        public double slipAngle;
        public double carSpeed;
        public double curveRadius;
        public double throttle;
        public double centerDistance;

        public DataSample(double slipAngle, double carSpeed,
                double curveRadius, double throttle, double centerDistance) {
            this.slipAngle = slipAngle;
            this.carSpeed = carSpeed;
            this.curveRadius = curveRadius;
            this.throttle = throttle;
            this.centerDistance = centerDistance;
        }

        @Override
        public String toString() {
            return String
                    .format("SlipAngle[%s] carSpeed[%s] curveRadius[%s] throttle[%s] centerDistance[%s]",
                            slipAngle, carSpeed, curveRadius, throttle,
                            centerDistance);
        }

        public DataSample copy() {
            return new DataSample(slipAngle, carSpeed, curveRadius, throttle,
                    centerDistance);
        }
    }

}
