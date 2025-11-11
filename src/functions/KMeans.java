package functions;
/**
 *   CSCI 4372: Phase 2: KMeans
 *      Author: Seth Garner
 *        Date: <2025-09-28 Sun>
 * Style Guide: Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                   * Will find the kmeans of one run and return sse for that run
 *              }
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import objects.AppObj;
import objects.RunResult;

public class KMeans {
    /**
     * @param  appObj   - Used to get the data from the file
     * @param  randSeed - The random seed passed to the function
     * @return sse      - The best sse found for this run
     */

    // This function will run perform one K-Means run run and return the sse
public static double runOne(AppObj appObj,
                            Random randSeed,
                            List<String> runOutput) {
    List<double[]> data = appObj.getData();
    int dataSize = data.size();
    int numOfClusters = appObj.getNoOfClusters();
    int maxIterations = appObj.getNoOfMaxIterations();
    double convergence = appObj.getConvergenceThreshold();

    if (numOfClusters > dataSize) {
        System.err.println("ERROR: Number of clusters must be <= data size");
        return Double.POSITIVE_INFINITY;
    }

    int dataLength = appObj.getNoOfColumns();
    List<double[]> centers = initCenters(data, numOfClusters, randSeed);
    double prevSSE = Double.POSITIVE_INFINITY;
    double sse = Double.POSITIVE_INFINITY;

    // --- NEW: compute Initial SSE before first iteration ---
    int[] initialAssign = assignPoints(data, centers);
    double initialSSE = computeSSE(data, centers, initialAssign);
    runOutput.add(String.format("Initial SSE: %.6f\n", initialSSE));

    int iterations = 0;

    // k means loop
    for (int t = 1; t <= maxIterations; t++) {
        iterations++;
        int[] assign = assignPoints(data, centers);
        sse = computeSSE(data, centers, assign);

        runOutput.add(String.format("Iteration %d: SSE = %.6f\n", t, sse));

        if (prevSSE != Double.POSITIVE_INFINITY) {
            double improved = (prevSSE - sse) / prevSSE;
            if (improved < convergence) {
                break;
            }
        }
        centers = recomputeCenters(data, assign, numOfClusters, dataLength, centers);
        prevSSE = sse;
    }

    // --- NEW: mark final results ---
    runOutput.add(String.format("Final SSE: %.6f\n", sse));
    runOutput.add(String.format("Iterations: %d\n", iterations));

    return sse;
}

public static RunResult runOneDetailed(AppObj appObj,
                                       Random randSeed,
                                       List<String> runOutput) {
    List<double[]> data = appObj.getData();
    int dataSize = data.size();
    int numOfClusters = appObj.getNoOfClusters();

    int maxIterations = appObj.getNoOfMaxIterations();
    if (maxIterations <= 0) maxIterations = 100;

    double convergence = appObj.getConvergenceThreshold();
    if (convergence < 0.0) convergence = 0.0;

    if (numOfClusters > dataSize) {
        System.err.println("ERROR: Number of clusters must be <= data size");
        return new RunResult(Double.NaN, Double.NaN, 0);
    }

    int dataLength = appObj.getNoOfColumns();
    List<double[]> centers = initCenters(data, numOfClusters, randSeed);
    double prevSSE = Double.POSITIVE_INFINITY;
    double sse = Double.POSITIVE_INFINITY;

    // --- Compute Initial SSE ---
    int[] initialAssign = assignPoints(data, centers);
    double initialSSE = computeSSE(data, centers, initialAssign);
    runOutput.add(String.format("Initial SSE: %.6f\n", initialSSE));

    int[] assign = initialAssign; // keep track of current assignment
    int iterations = 0;

    // --- Main loop ---
    for (int t = 1; t <= maxIterations; t++) {
        iterations++;
        assign = assignPoints(data, centers);
        sse = computeSSE(data, centers, assign);

        runOutput.add(String.format("Iteration %d: SSE = %.6f\n", t, sse));

        if (prevSSE != Double.POSITIVE_INFINITY) {
            double improved = (prevSSE - sse) / prevSSE;
            if (improved < convergence) {
                break;
            }
        }

        centers = recomputeCenters(data, assign, numOfClusters, dataLength, centers);
        prevSSE = sse;
    }

    runOutput.add(String.format("Final SSE: %.6f\n", sse));
    runOutput.add(String.format("Iterations: %d\n", iterations));

    // --- Return a RunResult object ---

    RunResult runresult = new RunResult(initialSSE, sse, iterations);
    runresult.setFinalCenters(centers);
    runresult.setFinalAssign(assign);
    runresult.setShape(dataSize, dataLength);
    //return new RunResult(initialSSE, sse, iterations);

    return runresult;
}

    /**
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
            // copy so that we do not get alias of the originals
            centers.add(Arrays.copyOf(data.get(indices.get(i)), dataLength));
        }
        return centers;
    }

    /**
     *
     * @param  data    - The data for the run
     * @param  centers - The centers we have for the datalist
     * @return assign  - A parallel array for the data list
     *
     * This function will go through each row from the dataset. It will then loop through each
     * center, and will get the distance from the datarow and each center. It will loop through
     * until it finds the best distance. When it does, it will assign the bestK to the assigned
     * array which is parallel to the datasize.
     */
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
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestK = k;
                }
            }
            assign[i] = bestK;
        }
        return assign;
    }

    /**
     *
     * @param  data          - The dataset
     * @param  assign        - The assigned datapoints for centers
     * @param  numOfClusters - Number of K
     * @param  dataLength    - Length of the list of data
     * @param  oldCenters    - Original centers
     * @return centers       - The recalculated centers
     */
    public static List<double[]> recomputeCenters(List<double[]> data,
                                                  int[] assign,
                                                  int numOfClusters,
                                                  int dataLength,
                                                  List<double[]> oldCenters) {
        double[][] sums = new double[numOfClusters][dataLength]; // Holds the sum for coor of all points assinged for that cluster
        int[] counts = new int[numOfClusters];                   // How many points are for that cluster

        for (int i = 0; i < data.size(); i++) {
            int cluster = assign[i];        // What cluster this point is assigned to
            double[] dataRow = data.get(i); // The points coor in the list
            counts[cluster]++;              // Add one point to tthe cluster
            for (int j = 0; j < dataLength; j++) {
                sums[cluster][j] += dataRow[j]; // Add this point's coor into cluster's sums
            }
        }

        List<double[]> centers = new ArrayList<>(numOfClusters);
        for (int k = 0; k < numOfClusters; k++) {
            if (counts[k] == 0) {
                // keep old center if cluster is empty
                centers.add(Arrays.copyOf(oldCenters.get(k), dataLength));
            } else {
                double[] currentCenter = new double[dataLength]; //
                for (int d = 0; d < dataLength; d++) {
                    currentCenter[d] = sums[k][d] / counts[k];
                }
                centers.add(currentCenter);
            }
        }
        return centers;
    }

    /**
     *
     * @param  data    - The dataset
     * @param  centers - The centers that will be used
     * @param  assign  - The assigned points to the respective center
     * @return sse     - Returns the SSE of the rows and centers of this iteration
     */
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
    // Kept the variables non specific incase this method could be used in a future feature
    // This is the solution to not using the sqrt() or pow() function
    /**
     *
     * @param   a   - The first double[] array
     * @param   b   - The second double[] array
     * @return  sum - The sum found between
     */
    private static double sqDist(double[] a, double[] b) {
        double sum = 0.0;
        for (int d = 0; d < a.length; d++) {
            double diff = a[d] - b[d];
            sum += diff * diff;
        }
        return sum;
    }
}
