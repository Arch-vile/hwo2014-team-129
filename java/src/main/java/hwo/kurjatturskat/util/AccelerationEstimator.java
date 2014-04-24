package hwo.kurjatturskat.util;

import hwo.kurjatturskat.ai.behaviours.throttle.SpeedSampleCollectorBehaviour;

public class AccelerationEstimator {

    // Acceleration constant
    private Double A;

    private SpeedSampleCollectorBehaviour dragDataSampler;
    private DragEstimator dragEstimate;

    public AccelerationEstimator(SpeedSampleCollectorBehaviour dragDataSampler,
            DragEstimator dragEstimate) {
        this.dragDataSampler = dragDataSampler;
        this.dragEstimate = dragEstimate;
    }

    public Double estimateAccelerationConstant() {
        if (dragDataSampler.samplesReady() && A == null
                && this.dragEstimate.getD() != null) {
            double[] samplesOnFullThrottle = dragDataSampler
                    .getSpeedSamplesOnFullThrottle();
            A = estimateAccelarationConstant(samplesOnFullThrottle[0],
                    samplesOnFullThrottle[1]);
            System.out.println("Estimated acceleration constant:" + A);

        }

        return A;
    }

    public double estimateAccelarationConstant(double speedOnFirstTick,
            double speedOnSecondTick) {
        double lowerLimit = 0;
        double upperLimit = determineUpperLimit(speedOnFirstTick,
                speedOnSecondTick);

        while (true) {
            double errorOnLowerLimit = errorWithEstimatedAcceleration(
                    speedOnFirstTick, speedOnSecondTick, lowerLimit);
            double errorOnUpperLimit = errorWithEstimatedAcceleration(
                    speedOnFirstTick, speedOnSecondTick, upperLimit);

            double middle = ((upperLimit - lowerLimit) / 2d) + lowerLimit;
            if (errorOnUpperLimit < errorOnLowerLimit) {
                lowerLimit = middle;
            } else {
                upperLimit = middle;
            }

            double errorOnMiddle = errorWithEstimatedAcceleration(
                    speedOnFirstTick, speedOnSecondTick, middle);
            if (errorOnMiddle < 0.0000001) {
                return middle;
            }

        }
    }

    private double determineUpperLimit(double speedOnFirstTick,
            double speedOnSecondTick) {
        double minK = 0;
        double errorWithMinK = errorWithEstimatedAcceleration(speedOnFirstTick,
                speedOnSecondTick, minK);
        double step = 1;
        for (double k = step;; k += step) {
            double error = errorWithEstimatedAcceleration(speedOnFirstTick,
                    speedOnSecondTick, k);
            if (error >= errorWithMinK)
                return k;
        }
    }

    public double errorWithEstimatedAcceleration(double startSpeed,
            double targetSpeed, double dragEstimate) {
        return Math.abs(getSpeedOnNextTickWhenOnFullThrottle(dragEstimate,
                startSpeed) - targetSpeed);
    }

    private double getSpeedOnNextTickWhenOnFullThrottle(double K,
            double startSpeed) {
        double startSpeedForNextStep = startSpeed;

        double totalTime = DragEstimator.TIME_STEP;
        while (totalTime <= 1) {
            startSpeedForNextStep += speedIncrease(DragEstimator.TIME_STEP,
                    startSpeedForNextStep, K, 1);
            totalTime += DragEstimator.TIME_STEP;
        }
        return startSpeedForNextStep;
    }

    public double getSpeedOnNextTick(double startSpeed, double throttle) {
        if (A == null)
            return startSpeed;

        double startSpeedForNextStep = startSpeed;
        double totalTime = DragEstimator.TIME_STEP;
        while (totalTime <= 1) {
            startSpeedForNextStep += speedIncrease(DragEstimator.TIME_STEP,
                    startSpeedForNextStep, A, throttle);
            totalTime += DragEstimator.TIME_STEP;
        }
        return startSpeedForNextStep;
    }

    private double speedIncrease(double timeStep, double speed, double a,
            double throttle) {
        double result = throttle * a * timeStep - this.dragEstimate.getD()
                * speed * timeStep;
        return result;
    }

    public Double getA() {
        return A;
    }

    public boolean estimateDone() {
        return this.A != null;
    }

}
