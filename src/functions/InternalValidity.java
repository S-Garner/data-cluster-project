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

    private static double euclid(double [] a, double [] b){
        double distance = 0.0;
        for (int i = 0; i < a.length; i++) {
            double difference = a[i] - b[i];
            distance += difference * difference;
        }

        return MathFuncs.sqrt(distance);
    }


    private static double[] mean(List<double []> data) {
        int N = data.size();
        int D = data.get(0).length;

        double [] means = new double[D];

        for (double[] x : data) {
            for (int i = 0; i < D; i++) {
                means[i] += x[i];
            }
        }
        for (int i = 0; i < D; i++) {
            means[i] /= N;
        }

        return means;
    }

    /*
     * 
     */
    public static double calinskiHarabas(List<double[]> data,
                                         int[] assign,
                                         List<double[]> centers,
                                         double sse) {
        int N = data.size();
        int K = centers.size();
        double[] mu = mean(data);

        int[] counts = new int[K];
        for (int a : assign) {
            counts[a]++;
        }

        double trSB = 0.0;

        for (int i = 0; i < K; i++) {
            if (counts[i] == 0) {
                continue;
            }

            double sum = 0.0;
            double[] ck = centers.get(i);

            for (int j = 0; j < ck.length; j++) {
                double difference = ck[j] - mu[j];
                sum += difference * difference;
            }

            trSB += counts[i] * sum;
        }

        double num = trSB / MathFuncs.Max(1, (K - 1));
        double den = sse  / MathFuncs.Max(1, (N - K));

        return num / den;
    }

    public static double silhouette(List<double []> data, int[] assign, int K) {
        int N = data.size();

        List<List<Integer>> idx = new ArrayList<>(K);

        for (int i = 0; i < K; i++) {
            idx.add(new ArrayList<>());
        }

        for (int i = 0; i < N; i++) {
            idx.get(assign[i]).add(i);
        }

        double total = 0.0;
        for (int i = 0; i < N; i++) {
            int ci = assign[i];
            List<Integer> own = idx.get(ci);

            // a(i)
            double ai = 0.0;
            if (own.size() > 1) {
                for (int j : own) if (j != i) ai += euclid(data.get(i), data.get(j));
                ai /= (own.size() - 1);
            } else {
                ai = 0.0;
            }

            // b(i)
            double bi = Double.POSITIVE_INFINITY;
            for (int k = 0; k < K; k++) {
                if (k == ci || idx.get(k).isEmpty()) continue;
                double sum = 0.0;
                List<Integer> other = idx.get(k);
                for (int j : other) sum += euclid(data.get(i), data.get(j));
                double avg = sum / other.size();
                if (avg < bi) bi = avg;
            }

            double denom = MathFuncs.Max(ai, bi);
            double si = (denom == 0.0) ? 0.0 : (bi - ai) / denom;
            total += si;
        }
        return total / N;
    }
}
