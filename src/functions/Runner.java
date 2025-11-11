/**
 *   CSCI 4372: Phase 3: Initiatilization and Running
 *      Author: Seth Garner
 *        Date: <2025-10-19>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Will run the KMeans clustering based on the appObj information
 *                  * Will keep track of best run based on SSE
 *              }
 */

package functions;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import objects.AppObj;
import objects.RunResult;
import objects.RunSummary;

public class Runner {

    /**
     *
     * @param  appObj    - For run information
     * @return runOutPut - Used for the exporter
     *
     * Will go through the number of runs, it will store which run had the best SSE
     * and will return the string object runOutPut for exporting and displaying
     */
    public static List<String> runner(AppObj appObj) {
        int runs = appObj.getNoOfRuns();           // Get the number of runs
        double bestSSE = Double.POSITIVE_INFINITY; // Use Double.POSITIVE_INFINITY so that whatever value is given will always be smaller and replace it
        int bestRun = -1;

        List<String> runOutput = new ArrayList<>();

        long baseSeed = 42L; // Used for random generating

        for (int i = 1; i <= runs; i++) {
            runOutput.add("Run " + i + "\n");
            runOutput.add("-----\n");
            double sse = KMeans.runOne(appObj, new Random(baseSeed + i), runOutput);
            if (sse < bestSSE) {
                bestSSE = sse;
                bestRun = i;
            }
            runOutput.add("\n");
        }
        runOutput.add("Best Run: " + bestRun + ": SSE = " + bestSSE);

        return runOutput;
    }

    /*
     * Will run the KMeans clustering based on the appObj information
     * 
     * @param  appObj - For run information
     * @return RunResult - The best run result
     */
    public static RunResult runnerWithResult(AppObj appObj) {
        int runs = appObj.getNoOfRuns();
        double bestSSE = Double.POSITIVE_INFINITY;
        int bestRun = -1;

        List<String> runOutput = new ArrayList<>();
        long baseSeed = System.nanoTime();

        RunResult bestResult = null;

        for (int i = 1; i <= runs; i++) {
            runOutput.add("Run " + i + "\n");
            runOutput.add("-----\n");

            // Run once
            RunResult current = KMeans.runOneDetailed(appObj, new Random(baseSeed + i), runOutput);

            // Compare to best
            if (current.getFinalSSE() < bestSSE) {
                bestSSE = current.getFinalSSE();
                bestRun = i;
                bestResult = current;
            }

            runOutput.add("\n");
        }

        runOutput.add(String.format("Best Run: %d: SSE = %.6f", bestRun, bestSSE));

        return bestResult;
    }

    /*
     * Will run the KMeans clustering based on the appObj information
     * 
     * @param  appObj - For run information
     * @return RunSummary - The best run summary
     */
    public static RunSummary runnerWithSummary(AppObj appObj) {
        int runs = appObj.getNoOfRuns();

        double bestSSE = Double.POSITIVE_INFINITY;
        int bestRun = -1;

        RunResult bestResult = null;

        // Track best values and which run produced them
        double bestInitialSSE = Double.POSITIVE_INFINITY;
        double bestFinalSSE   = Double.POSITIVE_INFINITY;
        int bestIterations    = Integer.MAX_VALUE;

        int bestInitialRun = -1;
        int bestFinalRun   = -1;
        int bestIterRun    = -1;

        List<String> runOutput = new ArrayList<>();
        long baseSeed = System.nanoTime();

        for (int i = 1; i <= runs; i++) {
            runOutput.add("Run " + i + "\n");
            runOutput.add("-----\n");

            RunResult current = KMeans.runOneDetailed(appObj, new Random(baseSeed + i), runOutput); // Run once
            current.setRunNumber(i);

            if (current.getFinalSSE() < bestSSE) {
                bestSSE = current.getFinalSSE();
                bestRun = i;
                bestResult = current;
            }

            if (current.getInitialSSE() < bestInitialSSE) { // Track best initial SSE
                bestInitialSSE = current.getInitialSSE();
                bestInitialRun = i;
            }

            if (current.getFinalSSE() < bestFinalSSE) {    // Track best final SSE
                bestFinalSSE = current.getFinalSSE();
                bestFinalRun = i;
            }

            // Track smallest number of iterations
            if (current.getIterations() < bestIterations) {
                bestIterations = current.getIterations();
                bestIterRun = i;
            }

            runOutput.add("\n");
        }

        runOutput.add("========================================\n");
        runOutput.add(String.format("Best Initial SSE: %.6f (Run %d)\n", bestInitialSSE, bestInitialRun));
        runOutput.add(String.format("Best Final SSE:   %.6f (Run %d)\n", bestFinalSSE, bestFinalRun));
        runOutput.add(String.format("Best Iterations:  %d (Run %d)\n", bestIterations, bestIterRun));
        runOutput.add("========================================\n");

        RunResult summaryResult = new RunResult(bestInitialSSE, bestFinalSSE, bestIterations);
        summaryResult.setRunNumber(bestFinalRun);

        runOutput.add(String.format("Best Run: %d: SSE = %.6f", bestRun, bestSSE));

        // Return both in a RunSummary container
        return new RunSummary(summaryResult, runOutput, bestFinalRun);
    }

}
