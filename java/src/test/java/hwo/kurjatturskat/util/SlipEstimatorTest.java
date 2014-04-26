package hwo.kurjatturskat.util;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import hwo.kurjatturskat.ai.behaviours.throttle.SlipEstimatingBehaviour.DataSample;

import org.junit.Before;
import org.junit.Test;

public class SlipEstimatorTest {

    // Data is collected from one run on germany track from the first curve. Curve is to the right car is accelerating on throttle 0.8
    double DRAG_CONSTANT_SET1 = 0.020417451858520508;
    double ACCELERATION_CONSTANT_SET1 = 0.2046215534210205;

    // Set 1 - From the beginning of the curve
    DataSample sampleSet1Point1 = new DataSample(0.254278385439683,
            6.787866819282007, 100.0, 0.1, 20.0);
    DataSample sampleSet1Point2 = new DataSample(0.7447985819359407,
            6.81271825552569, 100.0, 0.1, 20.0);

    // Set 2 - From the middle of the curve
    DataSample sampleSet2Point1 = new DataSample(14.621014821710704,
            7.035217290678304, 100.0, 0.1, 20.0);
    DataSample sampleSet2Point2 = new DataSample(16.524856515908557,
            7.055069628015993, 100.0, 0.1, 20.0);

    // Set 3 - From the end of the curve
    DataSample sampleSet3Point1 = new DataSample(33.759092985948364,
            7.216624918393208, 100.0, 0.1, 20.0);
    DataSample sampleSet3Point2 = new DataSample(35.516920368719354,
            7.2328109005865855, 100.0, 0.1, 20.0);

    SlipEstimator slipEstimator;
    AccEstimatorProxy accEst;
    DragEstimatorProxy dragEst;

    @Before
    public void setup() {
        System.out.println("\n\nSetup test");
        dragEst = new DragEstimatorProxy();
        dragEst.setD(DRAG_CONSTANT_SET1);
        accEst = new AccEstimatorProxy(dragEst);
        accEst.setA(ACCELERATION_CONSTANT_SET1);
        this.slipEstimator = new SlipEstimator(accEst);
        this.slipEstimator.estimateSlipConstant(sampleSet1Point1,
                sampleSet1Point2);
    }

    @Test
    public void testSet1() {
        double actual = sampleSet1Point2.slipAngle;
        double estimated = slipEstimator.estimateSlipAngleOnNextTick(
                sampleSet1Point1.slipAngle, sampleSet1Point1.carSpeed,
                sampleSet1Point1.curveRadius, sampleSet1Point1.throttle,
                sampleSet1Point1.centerDistance);

        if (Math.abs(actual - estimated) > 0.0001) {
            System.out.println("Set1: FAIL");
            System.out.println("Expected: " + actual);
            System.out.println("Got: " + estimated);
            System.out.println("Error: " + Math.abs(actual - estimated));
        } else {
            System.out.println("Set1: OK");
        }

        assertThat(Math.abs(actual - estimated), lessThan(0.0001));
    }

    @Test
    public void testSet2() {
        double actual = sampleSet2Point2.slipAngle;
        double estimated = slipEstimator.estimateSlipAngleOnNextTick(
                sampleSet2Point1.slipAngle, sampleSet2Point1.carSpeed,
                sampleSet2Point1.curveRadius, sampleSet2Point1.throttle,
                sampleSet2Point1.centerDistance);

        if (Math.abs(actual - estimated) > 0.0001) {
            System.out.println("Set2: FAIL");
            System.out.println("Expected: " + actual);
            System.out.println("Got: " + estimated);
            System.out.println("Error: " + Math.abs(actual - estimated));
        } else {
            System.out.println("Set2: OK");
        }

        assertThat(Math.abs(actual - estimated), lessThan(0.0001));

    }

    @Test
    public void testSet3() {
        double actual = sampleSet3Point2.slipAngle;
        double estimated = slipEstimator.estimateSlipAngleOnNextTick(
                sampleSet3Point1.slipAngle, sampleSet3Point1.carSpeed,
                sampleSet3Point1.curveRadius, sampleSet3Point1.throttle,
                sampleSet3Point1.centerDistance);

        if (Math.abs(actual - estimated) > 0.0001) {
            System.out.println("Set3: FAIL");
            System.out.println("Expected: " + actual);
            System.out.println("Got: " + estimated);
            System.out.println("Error: " + Math.abs(actual - estimated));
        } else {
            System.out.println("Set3: OK");
        }

        assertThat(Math.abs(actual - estimated), lessThan(0.0001));

    }

    private class DragEstimatorProxy extends DragEstimator {
        public void setD(double d) {
            D = d;
        }
    }

    private class AccEstimatorProxy extends AccelerationEstimator {

        public AccEstimatorProxy(DragEstimator dragEst) {
            super(dragEst);
        }

        public void setA(double a) {
            A = a;
        }
    }
}
