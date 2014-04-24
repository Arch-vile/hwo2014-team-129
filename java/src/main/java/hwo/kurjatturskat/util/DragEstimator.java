package hwo.kurjatturskat.util;

import hwo.kurjatturskat.ai.behaviours.throttle.DragEstimateBehaviour;

public class DragEstimator {

    // Drag constant
    private Double D;

    private DragEstimateBehaviour dragDataSampler;

    public DragEstimator(DragEstimateBehaviour dragDataSampler) {
        this.dragDataSampler = dragDataSampler;
    }

    public Double estimateDragConstant() {
        if (dragDataSampler.samplesReady() && D == null) {
            double[] samplesOnZeroThrottle = dragDataSampler
                    .getSpeedSamplesOnZeroThrottle();
            D = estimateDragConstant(samplesOnZeroThrottle[0],
                    samplesOnZeroThrottle[1]);
            System.out.println("Estimated drag constant:" + D);
            System.out.println("Verifying....");

            for (int i = 0; i < dragDataSampler
                    .getRecorededValuesOnZeroThrottle().size() - 1; i++) {
                System.out.println("Expected\t"
                        + dragDataSampler.getRecorededValuesOnZeroThrottle()
                                .get(i + 1));
                System.out.println("Got\t\t"
                        + getSpeedOnNextTickWhenOnZeroThrottle(dragDataSampler
                                .getRecorededValuesOnZeroThrottle().get(i)));
            }
        }

        return D;
    }

    public double estimateDragConstant(double speedOnFirstTick,
            double speedOnSecondTick) {
        double lowerLimit = 0;
        double upperLimit = determineUpperLimit(speedOnFirstTick,
                speedOnSecondTick);

        while (true) {
            double errorOnLowerLimit = errorWithEstimatedDrag(speedOnFirstTick,
                    speedOnSecondTick, lowerLimit);
            double errorOnUpperLimit = errorWithEstimatedDrag(speedOnFirstTick,
                    speedOnSecondTick, upperLimit);

            double middle = ((upperLimit - lowerLimit) / 2d) + lowerLimit;
            if (errorOnUpperLimit < errorOnLowerLimit) {
                lowerLimit = middle;
            } else {
                upperLimit = middle;
            }

            double errorOnMiddle = errorWithEstimatedDrag(speedOnFirstTick,
                    speedOnSecondTick, middle);
            if (errorOnMiddle < 0.00001) {
                return middle;
            }

        }
    }

    private double determineUpperLimit(double speedOnFirstTick,
            double speedOnSecondTick) {
        double minK = 0;
        double errorWithMinK = errorWithEstimatedDrag(speedOnFirstTick,
                speedOnSecondTick, minK);
        double step = 1;
        for (double k = step;; k += step) {
            double error = errorWithEstimatedDrag(speedOnFirstTick,
                    speedOnSecondTick, k);
            if (error >= errorWithMinK)
                return k;
        }
    }

    public double errorWithEstimatedDrag(double startSpeed, double targetSpeed,
            double dragEstimate) {
        return Math.abs(getSpeedOnNextTickWhenOnZeroThrottle(dragEstimate,
                startSpeed) - targetSpeed);
    }

    private double getSpeedOnNextTickWhenOnZeroThrottle(double K,
            double startSpeed) {
        double startSpeedForNextStep = startSpeed;
        double timeStep = 1d / 2000d;
        double totalTime = timeStep;
        while (totalTime <= 1) {
            startSpeedForNextStep += speedDecrease(timeStep,
                    startSpeedForNextStep, K);
            totalTime += timeStep;
        }
        return startSpeedForNextStep;
    }

    public double getSpeedOnNextTickWhenOnZeroThrottle(double startSpeed) {
        if (D == null)
            return startSpeed;

        double startSpeedForNextStep = startSpeed;
        double timeStep = 1d / 2000d;
        double totalTime = timeStep;
        while (totalTime <= 1) {
            startSpeedForNextStep += speedDecrease(timeStep,
                    startSpeedForNextStep, D);
            totalTime += timeStep;
        }
        return startSpeedForNextStep;
    }

    private double speedDecrease(double timeStep, double speed, double K) {
        double result = -1 * K * speed * timeStep;
        return result;
    }

    public double speedDecrease(double timeStep, double speed) {
        double result = -1 * D * speed * timeStep;
        return result;
    }

    public Double getD() {
        return D;
    }

    public boolean estimateDone() {
        return this.D != null;
    }

}
