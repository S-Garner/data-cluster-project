/**
 *   CSCI 4372: Phase 3
 *      Author: Seth Garner
 * Style Guide: Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Used to find the Calinski Harabas and the silhouttes for runs
 *              }
 */

package functions;

import java.util.ArrayList;
import java.util.List;

public class InternalValidity {

    /*
     * Helper function to calculate the euclidean distance between two lists of doubles
     * @param a, b
     * @return double
     */
    private static double euclid(double [] a, double [] b){
        double distance = 0.0;
        for (int i = 0; i < a.length; i++) {     // For each dimension
            double difference = a[i] - b[i];     // Difference between points
            distance += difference * difference;
        }

        return MathFuncs.sqrt(distance);         // Square root of distance
    }

    /*
     * A helper function to calculate the mean of a list of points.
     * @param data
     * @return double[]
     */
    private static double[] mean(List<double []> data) {
        int N = data.size();             // Number of points
        int D = data.get(0).length;      // Dimension of each point

        double [] means = new double[D]; // holder for the mean of each dimension

        for (double[] x : data) {
            for (int i = 0; i < D; i++) {
                means[i] += x[i];       // Sum of all values in the point
            }
        }
        for (int i = 0; i < D; i++) {
            means[i] /= N;             // Divide by the number of points
        }

        return means;
    }

    /*
     * Calculate the Calinski-Harabasz index
     * 
     * @param data, assign, centers, sse
     * @return double
     */
    public static double calinskiHarabas(List<double[]> data,
                                         int[] assign,
                                         List<double[]> centers,
                                         double sse) {
        int N = data.size();
        int K = centers.size();
        double[] mean = mean(data);

        int[] counts = new int[K];
        for (int a : assign) {
            counts[a]++;
        }

        double scatterBetween = 0.0;

        for (int i = 0; i < K; i++) {
            if (counts[i] == 0) {
                continue;
            }

            double sum = 0.0;
            double[] clusterCenters = centers.get(i);

            for (int j = 0; j < clusterCenters.length; j++) {
                double difference = clusterCenters[j] - mean[j];
                sum += difference * difference;
            }

            scatterBetween += counts[i] * sum;
        }

        double numerater = scatterBetween / MathFuncs.Max(1, (K - 1));
        double denominator = sse  / MathFuncs.Max(1, (N - K));

        return numerater / denominator;
    }

    /*
     * This will find the silhouette coefficient (a measure of how similar an object is to its own cluster compared to other clusters)
     * @param data, assign, K
     * @return double
     * 
     */
    public static double silhouette(List<double []> data, int[] assign, int K) {
        int N = data.size();

        List<List<Integer>> idx = new ArrayList<>(K);

        for (int i = 0; i < K; i++) {
            idx.add(new ArrayList<>()); // Create a list of indices for each cluster
        }

        for (int i = 0; i < N; i++) {
            idx.get(assign[i]).add(i); // Add the index of each point to its cluster
        }

        // Find euclidean distance between cluster and points
        double total = 0.0;
        for (int i = 0; i < N; i++) {
            int ci = assign[i];
            List<Integer> own = idx.get(ci);

            double intra = 0.0; // 
            if (own.size() > 1) {
                for (int j : own){
                    if (j != i) { 
                        intra += euclid(data.get(i), data.get(j)); // Euclidean distance
                    }
                } 
                intra /= (own.size() - 1);                         // Average distance
            } else {
                intra = 0.0;
            }


            double inter = Double.POSITIVE_INFINITY;
            for (int k = 0; k < K; k++) {
                if (k == ci || idx.get(k).isEmpty()) {
                    continue;
                }
                double sum = 0.0;
                List<Integer> other = idx.get(k);
                for (int j : other) {
                     sum += euclid(data.get(i), data.get(j)); // Euclidean distance
                }
                double avg = sum / other.size();             // Average distance
                if (avg < inter) inter = avg;
            }

            double denominator = MathFuncs.Max(intra, inter);
            double silhI;

            if (denominator == 0.0) {
                silhI = 0.0;
            } else {
                silhI = (inter - intra) / denominator;
            }

            total += silhI;
        }
        return total / N;
    }
}
