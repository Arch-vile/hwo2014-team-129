package hwo.kurjatturskat.util;

public class AccelerationEstimator {

    // Acceleration constant
    private Double A;

    private DragEstimator dragEstimator;

    public AccelerationEstimator(DragEstimator dragEstimator) {
        this.dragEstimator = dragEstimator;
    }

    public void estimateAccelerationConstant(double[] samplesOnFullThrottle) {
        if (this.dragEstimator.getD() != null) {
            A = estimateAccelarationConstant(samplesOnFullThrottle[0],
                    samplesOnFullThrottle[1]);
            System.out.println("Estimated acceleration constant:" + A);
        } else {
            System.err
                    .println("Trying to estimate acceleration constant before found drag costant!");
        }
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

    public double getSpeed(double startSpeed, double throttle, Integer timeSteps) {
        if (A == null)
            return startSpeed;

        double startSpeedForNextStep = startSpeed;
        double totalTime = DragEstimator.TIME_STEP;
        while (totalTime <= DragEstimator.TIME_STEP * timeSteps.doubleValue()) {
            startSpeedForNextStep += speedIncrease(DragEstimator.TIME_STEP,
                    startSpeedForNextStep, A, throttle);
            totalTime += DragEstimator.TIME_STEP;
        }
        return startSpeedForNextStep;
    }

    public double getSpeedOnNextTick(double startSpeed, double throttle) {
        return getSpeed(startSpeed, throttle,
                (int) (1d / DragEstimator.TIME_STEP));
    }

    // TODO: remove the getD() here and use the actual speed value from estimator instead
    private double speedIncrease(double timeStep, double speed, double a,
            double throttle) {
        double result = throttle * a * timeStep - this.dragEstimator.getD()
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
