/**
 *   CSCI 4372: Phase 1: Reading Inputs
 *      Author: Seth Garner
 *        Date: <2025-09-02 Tue>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Take the information given to the AppObj and converts it to usable data
 *                  * Will take doc object information and will parse through it using a rand function (may want to change)
 *                  * Will then take string rows and convert to doubles
 *                  * IF all information is correct pass to AppObj. Main appObj data object is in List<double> data
 *              }
 */


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class GetData {

    /**
     * Grabs the top row numbers from the file sets the row and column variables
     *
     * @param  appObj - The main app object
     * @return void
     */
    public static void setRowsAndColumnsNo(AppObj appObj) {

        Scanner scanner; // Create the scanner

        try
        {
            scanner = new Scanner(appObj.getFile());  // Grab the file

            appObj.setNoOfRows   (scanner.nextInt()); // Grab the 1st int on the first row, set as row
            appObj.setNoOfColumns(scanner.nextInt()); // Grab the 2nd int on the first row, set as columns

        } catch (FileNotFoundException e) {            // If file not found, throw error
            System.out.println("File not found");
        }

    }

    /**
     * Grabs the rows based on a random number generated then return List<double[]> from the loadData function
     *
     * @param  appObj         - The main app object
     * @return List<double[]> -
     */
    public static List<double[]> getRows(AppObj appObj) {

        List<String> rows = new ArrayList<>(); // Holds the string[]

        try (Scanner scanner = new Scanner(appObj.getFile()))      // Open the file
        {
            if (scanner.hasNextLine())                             // Check if file has nextline
            {
                scanner.nextLine();                                // Skip the first row; Data Description
            }

            List<String> allDataRows = new ArrayList<>();          // Holds all the rows

            while (scanner.hasNextLine())                          // While there's rows to grab
            {
                allDataRows.add(scanner.nextLine());               // Add the row to the data list
            }

            for (int i = 0; i < appObj.getNoOfClusters(); i++)     // Loop through the NUMBER OF CLUSTERS
            {

                int index = getRandomNum(1, appObj.getNoOfRows()); // Grab random number based on MIN and MAX
                rows.add(allDataRows.get(index - 1));              // Grab the applicable row minus element

            }

        } catch (FileNotFoundException e) {                        // If file not  found, throw error
            System.out.println("File not found");
        }

        return (loadData(rows, appObj.getNoOfColumns()));          // Return the double[] based on loadData function
    }

    /**
     * Get the random number according to min and max and return public it
     *
     * @param  min - Floor of the random generator
     * @param  max - Ceiling of the random generator
     * @return int - Return the random number
     */
    public static int getRandomNum(int min, int max) {

        Random random = new Random();               // Start rand
        return random.nextInt(max - min + 1) + min; // Set floor and ceiling of random numbers

    }

    /**
     * Will convert the String rows into List of doubles[]
     *
     * @param rows - The list of strings taken from the file
     * @param columns - The number of columns
     * @return List<double[]> - The data converted to doubles from file
     */
    public static List<double[]> loadData(List<String> rows, int columns) {

        List<double[]> outData = new ArrayList<>(rows.size()); // Start the double[] list
        int lineNo = 0;                                        // Will be used for checking nextLine

        for (String line : rows)                                      // Loop throw the rows
        {

            lineNo++;                                                 // Check for the next line

            if (line == null || line.trim().isEmpty())                // If next line is empty, break
            {
                break;
            }

            double[] lineArray = new double[columns];                 // Holds the current rows doubles

            try (Scanner scan = new Scanner(line))                    // Create scanner for current line
            {
                int j = 0;                                            // Create columns iterators

                while (j < columns && scan.hasNext())                 // Check that column exist
                {

                    if (!scan.hasNextDouble())                        // If there isn't a column that was expected
                    {
                        throw new IllegalArgumentException("Non-numeric token on line " + lineNo + ": " + line);
                    }

                    lineArray[j++] = scan.nextDouble();               // Add column

                }

                if (j != columns)                                     // If there isn't anymore columns
                {
                    throw new IllegalArgumentException("Error: Expected " + columns + " values, got " + j + " on line " + lineNo);
                }

                if (scan.hasNext())                                   // If there is too many elements than expected
                {
                    throw new IllegalArgumentException("Extra values after " + columns + " values on line " + lineNo + ": " + line);
                }
            }

            outData.add(lineArray);                                   // Add to List<double[]>
        }

        return outData;                                               // Return List<double[]>
    }

}
