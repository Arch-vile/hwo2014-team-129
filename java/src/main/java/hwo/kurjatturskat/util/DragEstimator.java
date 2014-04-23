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
            D = estimateDragConstant(samplesOnZeroThrottle[1],
                    samplesOnZeroThrottle[0]);
            System.out.println("Estimated drag constant:" + D);
        }

        return D;
    }

    public double estimateDragConstant(double currentSpeed, double lastSpeed) {
        double dragConstant = 0;
        double lastError = 10000;
        while (true) {

            double result = getSpeedOnNextTickWhenOnZeroThrottle(dragConstant,
                    lastSpeed);
            double error = Math.abs(result - currentSpeed);

            if (error < 0.000001) {
                break;
            }

            if (error < lastError) {
                lastError = error;
            } else {
                break;
            }

            dragConstant += 0.0000005;
        }

        return dragConstant;
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
