import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeans {
    /**
     * @param  appObj   - Used to get the data from the file
     * @param  randSeed - The random seed passed to the function
     * @return sse      - The best sse found for this run
     */

    // This function will run perform one K-Means run run and return the sse
    public static double runOne(AppObj appObj, Random randSeed) {
        List<double[]> data = appObj.getData();                // get the data for this run
        int dataSize = data.size();                            //
        int numOfClusters = appObj.getNoOfClusters();          //
        int maxIterations = appObj.getNoOfMaxIterations();     //
        double convergence = appObj.getConvergenceThreshold(); //

        if (numOfClusters > dataSize) {
            System.err.println("ERROR: Number of clusters must be <= data size");
            return Double.POSITIVE_INFINITY;
        }

        int dataLength = data.get(0).length;
        List<double[]> centers = initCenters(data, numOfClusters, randSeed);
        double prevSSE = Double.POSITIVE_INFINITY;
        double sse = Double.POSITIVE_INFINITY;

        for (int t = 1; t <= maxIterations; t++) {
            int[] assign = assignPoints(data, centers);                                // assign points to each center
            List<double[]> newCenters = recomputeCenters(data,
                                                         assign,
                                                         numOfClusters,
                                                         dataLength,
                                                         centers); // recompute
            sse = computeSSE(data, newCenters, assign);                                // SSE at end of iteration

            System.out.println("Iteration " + t + ": SSE = " + sse);

            if (prevSSE != Double.POSITIVE_INFINITY) {
                double improved = (prevSSE - sse) / prevSSE; // Improvements
                if (improved < convergence) {                // If it converged, break
                    centers = newCenters;
                    break;
                }
            }
            prevSSE = sse;
            centers = newCenters;
        }
        return sse;
    }

    /*
     *
     * @param  data          - Data that will be used
     * @param  numOfClusters - Number of clusters
     * @param  randSeed      - Randomized seed
     * @return centers       - Returns the randomly selected centers
     */

    // This function takes in the data set, and using the random seed, creates indices
    // And uniformly randomly selects the centers, then returns the List of centers
    public static List<double[]> initCenters(List<double[]> data, int numOfClusters, Random randSeed) {
        int dataSize = data.size();
        int dataLength = data.get(0).length;

        List<Integer> indices = new ArrayList<>(dataSize);
        for (int i = 0; i < dataSize; i++) { // Create indices for random selection
            indices.add(i);
        }
        Collections.shuffle(indices, randSeed);       // Shuffle those indices

        List<double[]> centers = new ArrayList<>(numOfClusters);
        for (int i = 0; i < numOfClusters; i++) {
            // copy so we don’t alias original rows
            centers.add(Arrays.copyOf(data.get(indices.get(i)), dataLength));
        }
        return centers;
    }

    public static int[] assignPoints(List<double[]> data, List<double[]> centers) {
        int dataSize = data.size();
        int centerSize = centers.size();
        int[] assign = new int[dataSize];

        for (int i = 0; i < dataSize; i++) {
            double[] dataRow = data.get(i);
            double bestDistance = Double.POSITIVE_INFINITY;
            int bestK = 0;

            for (int k = 0; k < centerSize; k++) {
                double distance = sqDist(dataRow, centers.get(k));
                // tie-break to smallest k
                if (distance < bestDistance || (distance == bestDistance && k < bestK)) {
                    bestDistance = distance;
                    bestK = k;
                }
            }
            assign[i] = bestK;
        }
        return assign;
    }

    public static List<double[]> recomputeCenters(List<double[]> data,
                                                  int[] assign,
                                                  int numOfClusters,
                                                  int dataLength,
                                                  List<double[]> oldCenters) {
        double[][] sums = new double[numOfClusters][dataLength];
        int[] counts = new int[numOfClusters];

        for (int i = 0; i < data.size(); i++) {
            int cluster = assign[i];
            double[] dataRow = data.get(i);
            counts[cluster]++;
            for (int j = 0; j < dataLength; j++) {
                sums[cluster][j] += dataRow[j];
            }
        }

        List<double[]> centers = new ArrayList<>(numOfClusters);
        for (int k = 0; k < numOfClusters; k++) {
            if (counts[k] == 0) {
                // keep old center if cluster is empty (base spec behavior)
                centers.add(Arrays.copyOf(oldCenters.get(k), dataLength));
            } else {
                double[] currentCenter = new double[dataLength];
                for (int d = 0; d < dataLength; d++) {
                    currentCenter[d] = sums[k][d] / counts[k];
                }
                centers.add(currentCenter);
            }
        }
        return centers;
    }

    public static double computeSSE(List<double[]> data,
                                    List<double[]> centers,
                                    int[] assign) {
        double sse = 0.0;
        for (int i = 0; i < data.size(); i++) {
            sse += sqDist(data.get(i), centers.get(assign[i]));
        }
        return sse;
    }

    // Finds the distance squared based on two double arrays, a & b
    private static double sqDist(double[] a, double[] b) {
        double sum = 0.0;
        for (int d = 0; d < a.length; d++) {
            double diff = a[d] - b[d];
            sum += diff * diff;
        }
        return sum;
    }
}
