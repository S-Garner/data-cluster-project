import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeans {
    public static double runOne(AppObj appObj, Random rng) {
        List<double[]> data = appObj.getData();
        int N = data.size();
        int K = appObj.getNoOfClusters();
        int I = appObj.getNoOfMaxIterations();
        double T = appObj.getConvergenceThreshold();

        if (K > N) {
            System.err.println("ERROR: Number of clusters must be <= data size");
            return Double.POSITIVE_INFINITY;
        }

        int D = data.get(0).length; // safer than header
        List<double[]> centers = initCenters(data, K, rng);

        double prevSSE = Double.POSITIVE_INFINITY;
        double sse = Double.POSITIVE_INFINITY;

        for (int t = 1; t <= I; t++) {
            int[] assign = assignPoints(data, centers);                                // assign points to each center
            List<double[]> newCenters = recomputeCenters(data, assign, K, D, centers); // recompute
            sse = computeSSE(data, newCenters, assign);                                // SSE at end of iteration

            System.out.println("Iteration " + t + ": SSE = " + sse);

            if (prevSSE != Double.POSITIVE_INFINITY) {
                double improved = (prevSSE - sse) / prevSSE;       // relative improvement
                if (improved < T) { centers = newCenters; break; } // converged
            }

            prevSSE = sse;
            centers = newCenters;
        }
        return sse;
    }

    public static List<double[]> initCenters(List<double[]> data, int k, Random rng) {
        int N = data.size();
        int D = data.get(0).length;

        List<Integer> idx = new ArrayList<>(N);
        for (int i = 0; i < N; i++) idx.add(i);
        Collections.shuffle(idx, rng);

        List<double[]> centers = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            // copy so we don’t alias original rows
            centers.add(Arrays.copyOf(data.get(idx.get(i)), D));
        }
        return centers;
    }

    public static int[] assignPoints(List<double[]> data, List<double[]> centers) {
        int N = data.size();
        int K = centers.size();
        int[] assign = new int[N];

        for (int i = 0; i < N; i++) {
            double[] p = data.get(i);
            double best = Double.POSITIVE_INFINITY;
            int bestK = 0;

            for (int k = 0; k < K; k++) {
                double d = sqDist(p, centers.get(k));
                // tie-break to smallest k
                if (d < best || (d == best && k < bestK)) {
                    best = d;
                    bestK = k;
                }
            }
            assign[i] = bestK;
        }
        return assign;
    }

    public static List<double[]> recomputeCenters(List<double[]> data,
                                                  int[] assign,
                                                  int K,
                                                  int D,
                                                  List<double[]> oldCenters) {

        double[][] sums = new double[K][D];
        int[] counts = new int[K];

        for (int i = 0; i < data.size(); i++) {
            int k = assign[i];
            double[] p = data.get(i);
            counts[k]++;
            for (int d = 0; d < D; d++) {
                sums[k][d] += p[d];
            }
        }

        List<double[]> centers = new ArrayList<>(K);
        for (int k = 0; k < K; k++) {
            if (counts[k] == 0) {
                // keep old center if cluster is empty (base spec behavior)
                centers.add(Arrays.copyOf(oldCenters.get(k), D));
            } else {
                double[] c = new double[D];
                for (int d = 0; d < D; d++) {
                    c[d] = sums[k][d] / counts[k];
                }
                centers.add(c);
            }
        }
        return centers;
    }

    public static double computeSSE(List<double[]> data, List<double[]> centers, int[] assign) {
        double sse = 0.0;
        for (int i = 0; i < data.size(); i++) {
            sse += sqDist(data.get(i), centers.get(assign[i]));
        }
        return sse;
    }

    // helper: squared Euclidean distance (no sqrt, no pow)
    private static double sqDist(double[] a, double[] b) {
        double sum = 0.0;
        for (int d = 0; d < a.length; d++) {
            double diff = a[d] - b[d];
            sum += diff * diff;
        }
        return sum;
    }
}
