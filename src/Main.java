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
 *     Compile: javac -d out src/*.java {OR} ./build -> Will also run test
 *     Compile (New): javac -d out $(find src -name "*.java")
 *         Run: java -cp out Main {file:filename} {int:no_clusters} {int:no_max_itr} {double:conv_thresh} {int:no_runs}
 *         
 *         FOR PHASE 4
 *         Run (New): java -cp out Main {file:filename} phase4
 */

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import data.DataGetter;
import functions.ArgUtils;
import functions.ExportData;
import functions.MathFuncs;
import functions.Runner;
import functions.ValidatorRunner;
import objects.AppObj;
import objects.RunResult;
import objects.RunSummary;

public class Main {

    public static void main(String[] args) {
        // If user calls Phase 4 mode (args[1] == "phase4")
        if (args.length == 2 && args[1].equalsIgnoreCase("phase4")) {
            runPhase4(args[0]);
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
    private static void runPhase4(String dataFile) {
        System.out.println("Running Phase 4: Internal Validation...");
        AppObj appObj = new AppObj();
        appObj.setFile(new File(dataFile));

        // Load / normalize
        DataGetter.setRowsAndColumns(appObj);
        appObj.setData(DataGetter.normalize(DataGetter.getFullDataSet(appObj)));

        // Set the required paramaeters
        appObj.setNoOfMaxIterations(100);
        appObj.setConvergenceThreshold(0.001);
        appObj.setNoOfRuns(100);

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
        /*
        try {
            ExportData.writerPhase4(appObj.getFile().getName(), Kmin, Kmax, results);
            System.out.println("Phase 4 CSV written successfully.");
        } catch (IOException e) {
            System.out.println("Error writing Phase 4 output: " + e.getMessage());
        }
        */
    }

}

/**
 * NOTES FOR LATER:
 * May want to make it to where an object can hold different blocks. A vector of run objects, that way it can have
 * Multiple runs in the same obj
 */
