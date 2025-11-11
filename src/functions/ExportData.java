package functions;
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

import objects.AppObj;

public class ExportData {

    /*
     * Take AppObj double[] and write it to file
     *
     * @param appObj
     * @throws IOException
     */
    public static void writer (AppObj appObj, int runNumber)
        throws IOException
    {
        String base = removeTxt(appObj.getFile().getName()); // Removes .txt ext from the original file
        String file = addOutputTxt(base, runNumber);                    // Adds '-output.txt' to file

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

    public static void runWriter (AppObj appObj, List<String> runOutput, int runNumber)
        throws IOException
    {
        String base = removeTxt(appObj.getFile().getName());
        String file = addOutputTxt(base, runNumber);

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
    public static String addOutputTxt (String string, int runNumber) {
        if (runNumber > 0) {
            return string + "-output-" + runNumber + ".txt"; //
        } else {
        return string + "-output.txt";
        }
    }

    public static void writeSummary(AppObj appObj, List<String> table)
        throws IOException 
    {
        String base = removeTxt(appObj.getFile().getName());
        String file = base + "-results.csv";

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write("Dataset,Normalization,InitMethod,InitialSSE,FinalSSE,Iterations\n");
            for (String line : table)
                out.write(line);
        }
    }

    /*
     * @param appObj, method, runOutput, table
     * 
     * Write the results to a file in CSV format
     */
    public static void appendResults(AppObj appObj, String method,
                                 List<String> runOutput, List<String> table)
        throws IOException 
    {
        // extract last runs SSE and iteration count
        String dataset = appObj.getFile().getName();
        String normalization = "Min-Max";
        String finalLine = runOutput.get(runOutput.size() - 1);
        String finalSSE = finalLine.replaceAll("[^0-9\\.]", "");

        int iterations = 0;
        for (String line : runOutput)
            if (line.startsWith("Iteration"))
                iterations++;

        // approximate initial SSE from first iteration
        String initSSE = "";
        for (String line : runOutput) {
            if (line.startsWith("Iteration 1"))
                initSSE = line.replaceAll("[^0-9\\.]", "");
        }

        table.add(String.format("%s,%s,%s,%s,%s,%d%n",
                dataset, normalization, method, initSSE, finalSSE, iterations));
    }

    /*
     * @param dataset
     * @param Kmin
     * @param Kmax
     * @param rows
     * 
     * Will write the phase 4 results to a csv file
     */
    public static void writerPhase4(String dataset,
                                    int Kmin,
                                    int Kmax,
                                    List<ValidatorRunner.KResult> rows)
    throws IOException {
        String base = dataset.replace(".txt", "");
        String file = base + "-phase4.csv";

        System.out.println("Rows to write: " + rows.size());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Dataset,K,Index,Value,BestRun,BestFinalSSE\n");

        for (ValidatorRunner.KResult r : rows) {
            writer.write(String.format("%s,%d,CH,%.6f,%d,%.6f%n",
                    dataset, r.K, r.calinskiH, r.bestRun, r.bestFinalSSE));
            writer.write(String.format("%s,%d,SW,%.6f,%d,%.6f%n",
                    dataset, r.K, r.silh, r.bestRun, r.bestFinalSSE));
            }
        }
    }
}
