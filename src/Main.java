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

public class Main {
    public static void main (String[] args) {

        AppObj appObj = ArgUtils.checkArgs(args); // Get Arguments

        if (appObj == null) {                     // Make sure object could be created
            return;
        }

        //GetData.setRowsAndColumnsNo(appObj);      // Set number of columns and rows
        //appObj.setData(GetData.getRows(appObj));  // Get get data rows

        DataGetter.setRowsAndColumns(appObj);
        appObj.setData(DataGetter.getFullDataSet(appObj));
        appObj.setCenters(DataGetter.getCenters(appObj));

        System.out.println("Showing data:\n");
        appObj.displayData(appObj.getData());

        System.out.println("Showing Centers:\n");
        appObj.displayData(appObj.getCenters());

        System.out.println("Showing Point : Center : Distance");

        int tally = 0;

        for (double[] pointRow : appObj.getData()) {
            System.out.println("Point: " + tally + "\n");

            System.out.print("Points: ");
            appObj.displayPoint(pointRow);
            for (int i = 0; i < appObj.getCenters().size(); i++) {
                System.out.print("Center: ");
                appObj.displayPoint(appObj.getCenters().get(i));
                System.out.println(MathFuncs.getCenterDistance(pointRow, appObj.getCenters().get(i)));
            }
            tally++;
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
