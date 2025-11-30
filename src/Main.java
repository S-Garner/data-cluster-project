/**
 *   CSCI 4372: Phase 1: Reading Inputs
 *      Author: Seth Garner
 *        Date: <2025-09-02 Tue>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Will grab file from input arg[0]. Will set no_rows && no_columns based on header info
 *                  * Will then pass that information to the appObj. This will store most of the information
 *                  * Will then get datasets and place into appObj for math functions
 *                  * {Cont.}
 *                  * Export Data to file
 *              }
 *     Compile: (OLD AFTER PHASE 2): javac -d out src/*.java {OR} ./build -> Will also run test
 *     Compile  (New):               javac -d out $(find src -name "*.java")
 *         Run:                      java -cp out Main {file:filename} {int:no_clusters} {int:no_max_itr} {double:conv_thresh} {int:no_runs}
 *         
 *         *** FOR PHASE 4 ***
 *         Run (New): java -cp out Main {file:filename} {int:no_clusters} {int:no_max_itr} {double:conv_thresh} {int:no_runs} phase4
 * 
 *         As you can tell, the number of clusters is redundant here, however is still required because of my poor design
 *         I'm sorry for the inconvenience, but it's a lot of work to change
 */

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import data.DataGetter;
import functions.ArgUtils;
import functions.ExportData;
import functions.MathFuncs;
import functions.Runner;
import functions.ValidatorRunner;
import objects.AppObj;
import objects.RunResult;
import objects.RunSummary;
import functions.KMeans;
import functions.ExternalValidity;

public class Main {

    public static void main(String[] args) {
        // If user calls Phase 4 mode (args[1] == "phase4")
        if (args.length > 0 && args[args.length - 1].equalsIgnoreCase("phase4")) {
            runPhase4(args); // first arg is dataset file
            return;
        } else if (args.length > 0 && args[args.length - 1].equalsIgnoreCase("phase5")) {
            runPhase5(args);
            return;
        } else {
            runPhase3(args);
        }
    }

    // ---------------------------- Phase 3 ----------------------------
    private static void runPhase3(String[] args) {
        AppObj appObj = ArgUtils.checkArgs(args);
        AppObj appObj2 = ArgUtils.checkArgs(args);

        if (appObj == null) return;

        DataGetter.setRowsAndColumns(appObj);
        DataGetter.setRowsAndColumns(appObj2);

        appObj.setData(DataGetter.normalize(DataGetter.getFullDataSet(appObj)));
        appObj2.setData(DataGetter.normalize(DataGetter.getFullDataSet(appObj2)));

        appObj.setCenters(DataGetter.getCenters(appObj));           // Random selection
        appObj2.setCenters(DataGetter.randomPartitions(appObj2));   // Random partition

        RunSummary summary1 = Runner.runnerWithSummary(appObj);
        RunSummary summary2 = Runner.runnerWithSummary(appObj2);

        RunResult bestResult = summary1.getResult();
        RunResult bestResult2 = summary2.getResult();

        System.out.println(appObj.getFile());
        System.out.printf("Best Run: %d, Initial SSE: %.6f, Final SSE: %.6f, Iterations: %d%n",
                bestResult.getRunNumber(),
                bestResult.getInitialSSE(),
                bestResult.getFinalSSE(),
                bestResult.getIterations());

        try { ExportData.runWriter(appObj, summary1.getRunOutput(), 0); }
        catch (IOException e) { System.out.println(e); }

        System.out.printf("Best Run: %d, Initial SSE: %.6f, Final SSE: %.6f, Iterations: %d%n",
                bestResult2.getRunNumber(),
                bestResult2.getInitialSSE(),
                bestResult2.getFinalSSE(),
                bestResult2.getIterations());

        try { ExportData.runWriter(appObj2, summary2.getRunOutput(), 1); }
        catch (IOException e) { System.out.println(e); }
    }

    // ---------------------------- Phase 4 ----------------------------
    private static void runPhase4(String[] args) {
        System.out.println("Running Phase 4: Internal Validation...");

        String[] phaseArgs = new String[args.length - 1];
        System.arraycopy(args, 0, phaseArgs, 0, args.length - 1);

        AppObj appObj = ArgUtils.checkArgs(phaseArgs);
        if (appObj == null) return;

        // Load / normalize
        DataGetter.setRowsAndColumns(appObj);
        appObj.setData(DataGetter.normalize(DataGetter.getFullDataSet(appObj)));

        String dataFile = appObj.getFile().getName();

        int N = appObj.getNoOfRows();
        int Kmin = 2;
        int Kmax = (int)MathFuncs.Round(MathFuncs.sqrt(N / 2.0));
        int R = 100;

        System.out.printf("Dataset: %s | N=%d | K range: %d–%d%n", dataFile, N, Kmin, Kmax);

        LocalTime startTime = LocalTime.now();
        System.out.printf("Start time: %s%n", startTime);

        List<ValidatorRunner.KResult> results = ValidatorRunner.runner(appObj, Kmin, Kmax, R);

        LocalTime endTime = LocalTime.now();
        System.out.printf("End time: %s%n", LocalTime.now());

        Duration totalTime = Duration.between(startTime, endTime);
        System.out.printf("Total time: %d seconds%n%n", totalTime.getSeconds());
        
        try {
            ExportData.writerPhase4(appObj.getFile().getName(), Kmin, Kmax, results);
            System.out.println("Phase 4 CSV written successfully.");
        } catch (IOException e) {
            System.out.println("Error writing Phase 4 output: " + e.getMessage());
        }
        
    }

    private static void runPhase5(String[] args) {

        String[] phaseArgs = new String[args.length - 1];
        System.arraycopy(args, 0, phaseArgs, 0, args.length - 1);

        AppObj appObj = ArgUtils.checkArgs(phaseArgs);
        if (appObj == null) return;

        DataGetter.loadDataWithLabels(appObj);

        List<double[]> norm = DataGetter.normalize(appObj.getData());
        appObj.setData(norm);

        int K = appObj.getNoOfClusters();
        int[] truth = appObj.getTrueLabels();
        int R = appObj.getNoOfRuns();

        double bestRand = -1.0;
        double bestJaccard = -1.0;
        double bestFM = -1.0;

        int bestRandRun = -1;
        int bestJaccardRun = -1;
        int bestFMRun = -1;

    for (int r = 0; r < R; r++) {
    
        List<String> dummy = new ArrayList<>();
    
        RunResult runResult = KMeans.runOneDetailed(
            appObj,
            new Random(System.nanoTime() + r),
            dummy
        );
    
        int[] predicted = runResult.getFinalAssign();
    
        double rand = ExternalValidity.randIndex(truth, predicted);
        double jacc = ExternalValidity.jaccardIndex(truth, predicted);
        double fm   = ExternalValidity.fowlkesMallowsIndex(truth, predicted);
    
        if (rand > bestRand) {
            bestRand = rand;
            bestRandRun = r;
        }
        if (jacc > bestJaccard) {
            bestJaccard = jacc;
            bestJaccardRun = r;
        }
        if (fm > bestFM) {
            bestFM = fm;
            bestFMRun = r;
        }
    }

        ExportData.writePhase5(
            appObj.getFile().getName(),
            K,
            bestRand, bestRandRun,
            bestJaccard, bestJaccardRun,
            bestFM, bestFMRun
        );
    }


}

/**
 * NOTES FOR LATER:
 * May want to make it to where an object can hold different blocks. A vector of run objects, that way it can have
 * Multiple runs in the same obj
 */
