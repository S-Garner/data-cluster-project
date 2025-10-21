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
 *         Run: java -cp out Main {file:filename} {int:no_clusters} {int:no_max_itr} {double:conv_thresh} {int:no_runs}
 */

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main (String[] args) {

        AppObj appObj = ArgUtils.checkArgs(args); // Get Arguments
        AppObj appObj2 = ArgUtils.checkArgs(args);

        if (appObj == null) {                     // Make sure object could be created
            return;
        }

        DataGetter.setRowsAndColumns(appObj);
        DataGetter.setRowsAndColumns(appObj2);

        //appObj.setData(DataGetter.getFullDataSet(appObj));
        appObj.setData(DataGetter.normalize(DataGetter.getFullDataSet(appObj)));
        appObj2.setData(DataGetter.normalize(DataGetter.getFullDataSet(appObj2)));
        
        appObj.setCenters(DataGetter.getCenters(appObj));
        appObj2.setCenters(DataGetter.randomPartitions(appObj2));
        //appObj.setCenters(DataGetter.randomPartitions(appObj));
        
        List<String> runOutput = Runner.runner(appObj);
        RunResult bestResult = Runner.runnerWithResult(appObj);

        List<String> runOutput2 = Runner.runner(appObj2);
        RunResult bestResult2 = Runner.runnerWithResult(appObj2);

        System.out.printf(
        "Initial SSE: %.6f, Final SSE: %.6f, Iterations: %d\n"
        , bestResult.getInitialSSE(), bestResult.getFinalSSE(), bestResult.getIterations());

        try
        {
        ExportData.runWriter(appObj, runOutput, 0);
        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.printf(
        "Initial SSE: %.6f, Final SSE: %.6f, Iterations: %d\n"
        , bestResult2.getInitialSSE(), bestResult2.getFinalSSE(), bestResult.getIterations());

        try{
        ExportData.runWriter(appObj2, runOutput2, 1);
        } catch (IOException e) {
            System.out.println(e);
        }

        /*
        try
        {
        ExportData.writer(appObj);                // Write to file-output
        } catch (IOException e) {
            System.out.println(e);
        }
        */

    }
}

/**
 * NOTES FOR LATER:
 * May want to make it to where an object can hold different blocks. A vector of run objects, that way it can have
 * Multiple runs in the same obj
 */
