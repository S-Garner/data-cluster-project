/**
 *   CSCI 4372: Phase 1: Reading Inputs
 *      Author: Seth Garner
 *        Date: <2025-09-02 Tue>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Data exported. Will take an appObj, if the information is correct
 *                  * Will base the name of output text off of original filename
 *              }
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExportData {

    /*
     * Take AppObj double[] and write it to file
     *
     * @param appObj
     * @throws IOException
     */
    public static void writer (AppObj appObj)
        throws IOException
    {
        String base = removeTxt(appObj.getFile().getName()); // Removes .txt ext from the original file
        String file = addOutputTxt(base);                    // Adds '-output.txt' to file

        BufferedWriter outputWrite = new BufferedWriter(new FileWriter(file)); // Create the buffer writer

        for (double[] dataPoints : appObj.getData()) {                         // Loop through the List<double[]>
            for (int i = 0; i < dataPoints.length; i++) {                      // Loop through the double[]
                outputWrite.write(Double.toString(dataPoints[i]) + " ");       // Write "{double} ' '" to file
            }

            outputWrite.newLine();                           // Add the end of the line
        }
        outputWrite.flush();                                 // Clear out the writer
        outputWrite.close();                                 // Close out the writer
    }

    public static void runWriter (AppObj appObj, List<String> runOutput)
        throws IOException
    {
        String base = removeTxt(appObj.getFile().getName());
        String file = addOutputTxt(base);

        //BufferedWriter outputWrite = new BufferedWriter(new FileWriter(file));

        try (PrintWriter outputWriter = new PrintWriter(new FileWriter(file))) {
            for (String line : runOutput) {
                outputWriter.print(line);
            }
        }
    }

    /*
     * Remove the '.txt' from the file name
     *
     * @param file
     * @return String
     */
    public static String removeTxt (String file) {
        return file.replace(".txt", ""); // Return the removed string
    }

    /*
     * Add '-output.txt' to the file name
     *
     * @param string
     * @return string
     */
    public static String addOutputTxt (String string) {
        return string + "-output.txt"; //
    }
}
