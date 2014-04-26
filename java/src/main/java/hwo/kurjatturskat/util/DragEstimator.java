package hwo.kurjatturskat.util;

public class DragEstimator {

    public final static double TIME_STEP = 1d / 2000d;
    public static final double ONE_TICK = 1d;

    // Drag constant
    protected Double D;

    public void estimateDragConstant(double[] samplesOnZeroThrottle) {
        D = estimateDragConstant(samplesOnZeroThrottle[0],
                samplesOnZeroThrottle[1]);
        System.out.println("Estimated drag constant:" + D);
    }

    private double estimateDragConstant(double speedOnFirstTick,
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
            if (errorOnMiddle < 0.0000001) {
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
        double totalTime = TIME_STEP;
        while (totalTime <= 1) {
            startSpeedForNextStep += speedDecrease(TIME_STEP,
                    startSpeedForNextStep, K);
            totalTime += TIME_STEP;
        }
        return startSpeedForNextStep;
    }

    public double getSpeedOnNextTickWhenOnZeroThrottle(double startSpeed) {
        if (D == null)
            return startSpeed;

        double startSpeedForNextStep = startSpeed;
        double totalTime = TIME_STEP;
        while (totalTime <= 1) {
            startSpeedForNextStep += speedDecrease(TIME_STEP,
                    startSpeedForNextStep, D);
            totalTime += TIME_STEP;
        }
        return startSpeedForNextStep;
    }

    private double speedDecrease(double timeStep, double speed, double K) {
        double result = -1 * K * speed * timeStep;
        return result;
    }

    public Double getD() {
        return D;
    }

    public boolean estimateDone() {
        return this.D != null;
    }

}
