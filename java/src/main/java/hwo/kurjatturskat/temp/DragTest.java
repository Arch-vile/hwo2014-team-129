package hwo.kurjatturskat.temp;

public class DragTest {

    public static void main(String[] args) {

        /*
         * Recoreded Data for car when throtte is set to 0
         * 
         * On straight:
         * Piece: [0] speed[1.99268649250204] tick[11] 
         * Piece: [0] speed[1.9528327626519992] tick[12] 
         * Piece: [0] speed[1.9137761073989594] tick[13]
         * Piece: [0] speed[1.875500585250979] tick[14]
         * Piece: [0] speed[1.8379905735459623] tick[15]
         * Piece: [0] speed[1.8012307620750434] tick[16]
         * ...
         * On straight:
         * Piece: [0] speed[0.5929301282441202] tick[71]
         * Piece: [0] speed[0.5810715256792349] tick[72]
         * Piece: [0] speed[0.5694500951656494] tick[73]
         * ...
         * On curve:
         * Piece: [3] speed[2.2121096566165597] tick[78]
         * Piece: [3] speed[2.167867463484229] tick[79] 
         */

        double recordedData[] = { 1.99268649250204, 1.9528327626519992,
                1.9137761073989594, 1.875500585250979, 1.8379905735459623,
                1.8012307620750434 };

        double recordedData2[] = { 0.5929301282441202, 0.5810715256792349,
                0.5694500951656494 };

        double recordedData3[] = { 2.2121096566165597, 2.167867463484229 };

        DragTest test = new DragTest();

        double K = test.estimateK(recordedData[2], recordedData[1]);
        System.out.println("Estimated K: " + K + "\n\n");
        test.estimateK(recordedData[4], recordedData[3]);
        System.out.println("Estimated K: " + K);

        double estimated;
        estimated = getSpeedOnNextTickWhenOnZeroThrottle(K, recordedData[0]);
        System.out.println("Expected:\t\t" + recordedData[1]);
        System.out.println("Got:\t\t\t" + estimated);

        estimated = getSpeedOnNextTickWhenOnZeroThrottle(K, recordedData[3]);
        System.out.println("Expected:\t\t" + recordedData[4]);
        System.out.println("Got:\t\t\t" + estimated);

        estimated = getSpeedOnNextTickWhenOnZeroThrottle(K, recordedData2[0]);
        System.out.println("Expected:\t\t" + recordedData2[1]);
        System.out.println("Got:\t\t\t" + estimated);

        estimated = getSpeedOnNextTickWhenOnZeroThrottle(K, recordedData3[0]);
        System.out.println("Expected:\t\t" + recordedData3[1]);
        System.out.println("Got:\t\t\t" + estimated);
    }

    // We assume K >= 0 !!!
    public double estimateK(double target, double lastSpeed) {
        double lowerLimit = 0;
        double upperLimit = determineMaxK(target, lastSpeed);

        int count = 0;
        while (true) {
            double middle = ((upperLimit - lowerLimit) / 2d) + lowerLimit;
            System.out.println(lowerLimit + " -- " + upperLimit + " ::: "
                    + middle);
            count++;

            double errorOnLowerLimit = Math
                    .abs(getSpeedOnNextTickWhenOnZeroThrottle(lowerLimit,
                            lastSpeed) - target);
            double errorOnUpperLimit = Math
                    .abs(getSpeedOnNextTickWhenOnZeroThrottle(upperLimit,
                            lastSpeed) - target);
            double errorOnMiddle = Math
                    .abs(getSpeedOnNextTickWhenOnZeroThrottle(middle, lastSpeed)
                            - target);

            if (errorOnUpperLimit < errorOnLowerLimit) {
                lowerLimit = middle;
            } else {
                upperLimit = middle;
            }

            if (errorOnMiddle < 0.00001) {
                System.out.println("iteratiosns: " + count);
                return middle;
            }

        }
    }

    private double determineMaxK(double target, double lastSpeed) {
        double minK = 0;
        double errorWithMinK = Math.abs(getSpeedOnNextTickWhenOnZeroThrottle(
                minK, lastSpeed) - target);
        double step = 1;
        for (double k = step;; k += step) {
            double estimatedSpeed = getSpeedOnNextTickWhenOnZeroThrottle(k,
                    lastSpeed);
            double error = Math.abs(estimatedSpeed - target);
            if (error >= errorWithMinK)
                return k;
        }

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
