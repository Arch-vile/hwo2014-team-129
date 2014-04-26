package hwo.kurjatturskat.util;

import hwo.kurjatturskat.ai.behaviours.throttle.SlipEstimatingBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SlipEstimatingBehaviour.DataSample;

public class SlipEstimator {

    // Slip constant
    private Double S;

    // private double MAGIC_NUMBER = 1.2d;

    private AccelerationEstimator accelerationEstimator;

    public SlipEstimator(AccelerationEstimator accelerationEstimator) {
        this.accelerationEstimator = accelerationEstimator;
    }

    public void estimateSlipConstant(DataSample sample1, DataSample sample2) {
        if (this.accelerationEstimator.getA() != null) {
            S = estimateSlipConstantS(sample1, sample2);
            System.out.println("Estimated slip constant: " + S);
        } else {
            System.err
                    .println("Could not estimate slip constant as acceleration constant not yet found!");
        }

    }

    public Double getS() {
        return this.S;
    }

    // TODO: returning values off by about 1 degree.
    public double estimateSlipAngleOnNextTick(double slipAngle,
            double carSpeed, double curveRadius, double throttle,
            double centerDistance) {

        if (this.S == null) {
            System.err
                    .println("Trying to estimate slip angle before slip constant is defined");
            return slipAngle;
        }

        DataSample current = new SlipEstimatingBehaviour.DataSample(slipAngle,
                carSpeed, curveRadius, throttle, centerDistance);

        // TODO: remove the magic number
        return estimateSlipAngle(current, S);
    }

    private Double estimateSlipConstantS(DataSample sample1, DataSample sample2) {
        double lowerLimit = -10000;
        double upperLimit = 10000;

        while (true) {
            double errorOnLowerLimit = errorWithGuessSlip(sample1, sample2,
                    lowerLimit);
            double errorOnUpperLimit = errorWithGuessSlip(sample1, sample2,
                    upperLimit);

            double middle = ((upperLimit - lowerLimit) / 2d) + lowerLimit;
            if (errorOnUpperLimit < errorOnLowerLimit) {
                lowerLimit = middle;
            } else {
                upperLimit = middle;
            }

            double errorOnMiddle = errorWithGuessSlip(sample1, sample2, middle);

            if (errorOnMiddle < 0.0000001) {
                return middle;
            }
        }

    }

    private double errorWithGuessSlip(DataSample sample1, DataSample sample2,
            double guess) {
        return Math.abs(estimateSlipAngle(sample1, guess) - sample2.slipAngle);
    }

    private double estimateSlipAngle(DataSample sample, double guess) {
        DataSample sampleCopy = sample.copy(); // Copy as we modify
        //System.out.println("Guess: " + guess);
        double totalTime = DragEstimator.TIME_STEP;
        while (totalTime <= 1) {
            sampleCopy.slipAngle = nextAngle(sampleCopy, guess,
                    DragEstimator.TIME_STEP);
            sampleCopy.carSpeed = accelerationEstimator.getSpeed(
                    sampleCopy.carSpeed, sampleCopy.throttle, 1);
            totalTime += DragEstimator.TIME_STEP;
        }
        return sampleCopy.slipAngle;
    }

    private double nextAngle(DataSample sample, double slipConstant,
            double timeStep) {

        // https://www.wolframalpha.com/input/?i=+alpha%3D%28%28v^2%2FR%29*cos%28theta%29-C%29%2Fr
        // alpha = ( (v^2/R) * cos(theta) - C) / r 
        // alpha = slip kulmakiihtyvyys
        // v = auton nopeus
        // R = mutkan säde
        // theta = auton slip angle
        // r = vetoakselin etäisyys auton keskipisteestä
        // C = maaginen vakio

        double v = sample.carSpeed;
        double R = sample.curveRadius;
        double theta = Math.toRadians(sample.slipAngle);
        double r = sample.centerDistance;
        double C = slipConstant;

        double alpha = (((v * v) / R) * Math.cos(theta) - C) / r;

        // https://www.wolframalpha.com/input/?i=w%3Dw0+%2B+1%2F2*alpha*t^2
        // w = w0 + 1/2 * alpha * t^2
        double newAngle = sample.slipAngle + 0.5 * Math.toDegrees(alpha)
                * timeStep * timeStep;

        return newAngle;
    }

    public Double getSlipConstant() {
        return S;
    }
}
