package hwo.kurjatturskat.util;

public class DragEstimator {

    // Drag constant
    private Double D;

    public void estimateDragConstant(double currentSpeed, double lastSpeed,
            double lastThrottle) {
        if (D == null) {
            // Make sure we have the situation we wanted i.e no throttle and slowing
            if (lastThrottle == 0 && currentSpeed < lastSpeed) {
                D = estimateDragConstant(currentSpeed, lastSpeed);
                System.out.println("Estimated drag constant:" + D);
            }
        }
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

}
