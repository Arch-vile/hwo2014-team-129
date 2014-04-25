package hwo.kurjatturskat.util;

import hwo.kurjatturskat.ai.behaviours.throttle.SlipConstantEstimatorBehaviour;
import hwo.kurjatturskat.ai.behaviours.throttle.SlipConstantEstimatorBehaviour.DataSample;

public class SlipEstimator {

    // Slip constant
    private Double S;

    private double MAGIC_NUMBER = 1.2d;

    private AccelerationEstimator accelerationEstimator;

    public SlipEstimator(AccelerationEstimator accelerationEstimator) {
        this.accelerationEstimator = accelerationEstimator;
    }

    public void estimateSlipConstant(DataSample sample1, DataSample sample2) {
        System.out.println("estimateSlipConstant");
        System.out.println(sample1);
        System.out.println(sample2);
        if (this.S == null) {
            S = estimateSlipConstantS(sample1, sample2);
            System.out.println("Estimated slip constant: " + S);
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

        DataSample current = new SlipConstantEstimatorBehaviour.DataSample(
                slipAngle, carSpeed, curveRadius, throttle, centerDistance);

        // TODO: remove the magic number
        return estimateSlipAngle(current, S) - MAGIC_NUMBER;
    }

    private Double estimateSlipConstantS(DataSample sample1, DataSample sample2) {
        double lowerLimit = 0;
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

    private double estimateSlipAngle(DataSample sample1, double guess) {
        double startAngle = sample1.slipAngle;
        double startSpeed = sample1.carSpeed;

        double totalTime = DragEstimator.TIME_STEP;
        while (totalTime <= 1) {

            startAngle += angleIncrease(startAngle, sample1, startSpeed, guess,
                    DragEstimator.TIME_STEP);
            totalTime += DragEstimator.TIME_STEP;
            startSpeed = accelerationEstimator.getSpeed(startSpeed,
                    sample1.throttle, 1);
        }
        return startAngle;
    }

    private double angleIncrease(double startAngleDegrees, DataSample sample1,
            double startSpeed, double slipConstant, double timeStep) {
        double angleInRadians = Math.toRadians(startAngleDegrees);
        double angleIncreaseRadians = 0.5
                * (((startSpeed * startSpeed * Math.cos(angleInRadians)) / sample1.curveRadius) + slipConstant)
                * (timeStep * timeStep);
        return Math.toDegrees(angleIncreaseRadians);
    }

    public Double getSlipConstant() {
        return S;
    }
}
