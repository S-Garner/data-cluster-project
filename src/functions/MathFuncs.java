/**
 *   CSCI 4372: Phase 1: Reading Inputs
 *      Author: Seth Garner
 *        Date: <2025-09-02 Tue>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Basic math functions used in clustering,
 *              }
 */

package functions;

public class MathFuncs {

    /*
     * Get the distance between a center and a point
     * @param point  - The data point
     * @param center - The center point
     * @return double - The distance
     */
    public static double getPointDistance(double point, double center) { 
        double distance = point - center; // Get the distance
        return distance * distance;
    }

    /*
     * Get the distance between a center and a point
     * @param center - The center point
     * @param points - The data point
     * @return double - The distance
     */
    public static double getCenterDistance(double[] center, double[] points) {
        double distance = 0.0;

        for (int i = 0; i < center.length; i++) {
            distance += getPointDistance(points[i], center[i]);
        }

        return distance;
    }

    /*
     * @param number - The number to square root
     * @return double - The square root of the number
     * 
     * This method is used to get the square root of a number, or NaN if the number is not finite
     */
    public static double sqrt(double number) {
        if (Double.isNaN(number) || number < 0.0) {
            return Double.NaN;
        }
        if (number == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        if (number == 0.0 || number == -0.0) {
            return number;
        }

        double guess = number / 2.0;

        double previousGuess;

        final double TOLERANCE = 1e-18;

        do {
            previousGuess = guess;

            guess = (previousGuess + (number / previousGuess)) / 2.0;

            double difference = previousGuess - guess;

            if (difference < 0) {
                difference = -difference;
            }

            if (difference <= TOLERANCE) {
                break;
            }
        } while (true);

        return guess;
    }

    /*
     * @param a
     * @param b
     * @returns the maximum of two numbers
     */
    public static int Max(int a, int b){
        if (a >= b) {
            return a;
        } else {
            return b;
        }
    }

    public static double Max(double a, double b) {
        return (a >= b) ? a : b;
    }

    /*
     * @param value
     * @return the rounded version of the input
     */
    public static double Round(double value) {
        return (long) (value + 0.5);
    }

    public static int Round(float value) {
        return (int) (value + 0.5);
    }

}
