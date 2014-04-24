package hwo.kurjatturskat.util;

import hwo.kurjatturskat.ai.behaviours.throttle.DragEstimateBehaviour;

public class AccelerationEstimator {

    // Acceleration constant
    private Double A;

    private DragEstimateBehaviour dragDataSampler;
    private DragEstimator dragEstimate;

    public AccelerationEstimator(DragEstimateBehaviour dragDataSampler,
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
            System.out.println("Verifying....");

            for (int i = 0; i < dragDataSampler
                    .getRecorededValuesOnFullThrottle().size() - 1; i++) {
                System.out.println("Expected\t"
                        + dragDataSampler.getRecorededValuesOnFullThrottle()
                                .get(i + 1));
                System.out.println("Got\t\t"
                        + getSpeedOnNextTickWhenOnFullThrottle(dragDataSampler
                                .getRecorededValuesOnFullThrottle().get(i)));
            }
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
            if (errorOnMiddle < 0.00001) {
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
        double timeStep = 1d / 2000d;
        double totalTime = timeStep;
        while (totalTime <= 1) {
            startSpeedForNextStep += speedIncrease(timeStep,
                    startSpeedForNextStep, K);
            totalTime += timeStep;
        }
        return startSpeedForNextStep;
    }

    public double getSpeedOnNextTickWhenOnFullThrottle(double startSpeed) {
        if (A == null)
            return startSpeed;

        double startSpeedForNextStep = startSpeed;
        double timeStep = 1d / 2000d;
        double totalTime = timeStep;
        while (totalTime <= 1) {
            startSpeedForNextStep += speedIncrease(timeStep,
                    startSpeedForNextStep, A);
            totalTime += timeStep;
        }
        return startSpeedForNextStep;
    }

    private double speedIncrease(double timeStep, double speed, double a) {
        double result = a * timeStep - this.dragEstimate.getD() * speed
                * timeStep;
        return result;
    }

    public Double getA() {
        return A;
    }

    public boolean estimateDone() {
        return this.A != null;
    }

}
