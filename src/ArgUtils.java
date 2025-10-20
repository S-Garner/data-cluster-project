/**
 *   CSCI 4372: Phase 1: Reading Inputs
 *      Author: Seth Garner
 *        Date: <2025-09-02 Tue>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Will grab the information from the command line obj. Check validity of entered values
 *                  * If it passes, send to appObj
 *              }
 */

import java.io.File;

public class ArgUtils {

    private static final int ARG_AMNT = 5; // Max number of args

    /**
     * @param  args   - The arguments inputted from the cmd line
     * @return AppObj - The app object that will hold the info
     */
    public static AppObj checkArgs(String[] args)
    {
        boolean checkVars = true;         // Main bool to make sure everything is correct

        int noClusters      = 0;          // Number of clusters
        int noMaxIterations = 0;          // Number of max iterations
        int noRuns          = 0;          // Number of runs

        double covergenceThreshold = 0.0; // Convergence threshold

        if (args.length != ARG_AMNT) // Check no of arguments is correct
        {
            System.out.println("ERROR: Incorrect number of arguments.\n"); // If no of args incorrect, send message, quit
            return null;
        }

        File file = new File(args[0]); // Create the file

        if (!(file.exists() && file.isFile())) // Make sure the file exists
        {
            System.out.println("ERROR: File does not exist\n"); // Display error is file doesn't exist, exit
            return null;
        }

        try // Check that args 2, 3, 5 are integers
        {
            noClusters      = Integer.parseInt(args[1]);
            noMaxIterations = Integer.parseInt(args[2]);
            noRuns          = Integer.parseInt(args[4]);
        } catch (NumberFormatException e){ // If not correct, throw error
            System.out.println("Error: ARGS {2}, {3}, {5} should be integers.\n");
        }

        try // Check that arg 4 is a double
        {
            covergenceThreshold = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("Error: ARGS {4} should be a double.\n");
        }

        // If any passed incorrectly, then quit out
        checkVars &= checkNoClusters(noClusters);
        checkVars &= checkNoOfIteration(noMaxIterations);
        checkVars &= checkCoveregence(covergenceThreshold);
        checkVars &= noOfRuns(noRuns);

        if (!checkVars) {
            return null;
        }

        return new AppObj(file, noClusters, noMaxIterations, covergenceThreshold , noRuns); // Return the AppObj

    }

    private static boolean checkNoClusters (int noOfClusters)
    {
        if (noOfClusters <= 1) {
            System.out.println("Error: Number of clusters must be > 1");
            return false;
        }
        return true;
    }

    /**
     * Check if number of iterations is correct
     *
     * @param  maxNoIterations - Max number of iterations
     * @return boolean         - Is the correct format
     */
    private static boolean checkNoOfIteration (int maxNoIterations)
    {
        if (maxNoIterations <= 0) {
            System.out.println("Error: Number of iterations must be > 0");
            return false;
        }
        return true;
    }

    /**
     * Check if convergence is correct
     *
     * @param  convergenceThreshold - Convergence threshold
     * @return boolean              - Is the correct format
     */
    private static boolean checkCoveregence (double convergenceThreshold)
    {
        if (convergenceThreshold < 0.0) {
            System.out.println("Error: Convergence threshold must be >= 0.0");
            return false;
        }
        return true;
    }

    /*
     * Check if number of runs is correct
     *
     * @param  noRuns  - Number of runs
     * @return boolean - Is the correct format
     */
    private static boolean noOfRuns (int noRuns)
    {
        if (noRuns < 1) {
            System.out.println("Error: Number of runs must be >= 1");
            return false;
        }
        return true;
    }

    private static void printUsage() {
        System.out.println("Usage: java -cp out Main {file:filename} {int:no_clusters} {int:no_max_itr} {double:conv_thresh} {int:no_runs}");
    }

}
