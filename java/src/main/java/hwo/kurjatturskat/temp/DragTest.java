package hwo.kurjatturskat.temp;

public class DragTest {

    public static void main(String[] args) {

        /*
         * Recoreded Data for car when throtte is set to 0
         * 
         * Piece: [0] speed[1.99268649250204] tick[11] 
         * Piece: [0] speed[1.9528327626519992] tick[12] 
         * Piece: [0] speed[1.9137761073989594] tick[13]
         * Piece: [0] speed[1.875500585250979] tick[14]
         * Piece: [0] speed[1.8379905735459623] tick[15]
         * Piece: [0] speed[1.8012307620750434] tick[16]
         * ...
         * Piece: [0] speed[0.5929301282441202] tick[71]
         * Piece: [0] speed[0.5810715256792349] tick[72]
         * Piece: [0] speed[0.5694500951656494] tick[73]
         */

        double recordedData[] = { 1.99268649250204, 1.9528327626519992,
                1.9137761073989594, 1.875500585250979, 1.8379905735459623,
                1.8012307620750434 };

        double recordedData2[] = { 0.5929301282441202, 0.5810715256792349,
                0.5694500951656494 };

        double K = estimateK(recordedData[2], recordedData[1]);
        estimateK(recordedData[4], recordedData[3]);

        double estimated;
        estimated = getSpeedOnNextTickWhenOnZeroThrottle(K, recordedData[0]);
        System.out.println("\n\nEstimated K: " + K);
        System.out.println("Expected:\t\t" + recordedData[1]);
        System.out.println("Got:\t\t\t" + estimated);

        estimated = getSpeedOnNextTickWhenOnZeroThrottle(K, recordedData[3]);
        System.out.println("\n\nEstimated K: " + K);
        System.out.println("Expected:\t\t" + recordedData[4]);
        System.out.println("Got:\t\t\t" + estimated);

        estimated = getSpeedOnNextTickWhenOnZeroThrottle(K, recordedData2[0]);
        System.out.println("\n\nEstimated K: " + K);
        System.out.println("Expected:\t\t" + recordedData2[1]);
        System.out.println("Got:\t\t\t" + estimated);
    }

    public static double estimateK(double target, double lastSpeed) {
        double K = 0;
        double lastError = 10000;
        long iterations = 0;
        while (true) {

            double result = getSpeedOnNextTickWhenOnZeroThrottle(K, lastSpeed);
            double error = Math.abs(result - target);

            if (error < 0.000001) {
                System.out.println("Iteration: " + iterations);
                System.out.println("Target: " + target);
                System.out.println("Result: " + result);
                System.out.println("Error: " + error);
                System.out.println("K: " + K);
                break;
            }

            if (error < lastError) {
                lastError = error;
            } else {
                System.out.println("Got worse result. Exit");
                System.out.println("Iteration: " + iterations);
                System.out.println("Target: " + target);
                System.out.println("Result: " + result);
                System.out.println("Error: " + error);
                System.out.println("K: " + K);
                break;
            }

            K += 0.0000005;
            iterations++;
        }

        return K;
    }

    public static double getSpeedOnNextTickWhenOnZeroThrottle(double K,
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

    private static double speedDecrease(double timeStep, double speed, double K) {
        double result = -1 * K * speed * timeStep;
        return result;
    }
}
