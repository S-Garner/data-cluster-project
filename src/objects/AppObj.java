package objects;
/**
 *   CSCI 4372: Phase 1: Reading Inputs
 *      Author: Seth Garner
 *        Date: <2025-09-02 Tue>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * This will be the main driving object
 *                  * Will hold rows, columns, no_runs, iterations, cov_thres, filename, and DATA
 *                  * KEEP MATH AND LOGIC OUT OF HERE AND TRY TO USE STATIC FUNCTIONS FOR OPS
 *              }
 */


import java.io.File;
import java.util.List;

public class AppObj {

    private File               file;
    private int                noOfClusters;
    private int                noOfMaxIterations;
    private double             convergenceThreshold;
    private int                noOfRuns;
    private List<double[]>     data;
    private List<double[]>     centers;
    private int[]              trueLabels;

    private final int MIN = 1;

    private int noOfRows;
    private int noOfColumns;

    public AppObj() {
        file                 = new File("");
        noOfClusters         = 0;
        noOfMaxIterations    = 0;
        convergenceThreshold = 0;
        noOfRuns             = 0;
    }

    /**
     *
     * @param file
     * @param noOfClusters
     * @param noOfMaxIterations
     * @param convergenceThreshold
     * @param noOfRuns
     */
    public AppObj(File file,
           int    noOfClusters,
           int    noOfMaxIterations,
           double convergenceThreshold,
           int    noOfRuns)
    {
        this.file                 = file;
        this.noOfClusters         = noOfClusters;
        this.noOfMaxIterations    = noOfMaxIterations;
        this.convergenceThreshold = convergenceThreshold;
        this.noOfRuns             = noOfRuns;
    }

    public double         getConvergenceThreshold() { return convergenceThreshold; }
    public File           getFile()                 { return file; }
    public int            getNoOfClusters()         { return noOfClusters; }
    public int            getNoOfMaxIterations()    { return noOfMaxIterations; }
    public int            getNoOfRuns()             { return noOfRuns; }
    public int            getNoOfRows()             { return noOfRows; }
    public int            getNoOfColumns()          { return noOfColumns; }
    public int            getMin()                  { return MIN; }
    public List<double[]> getData()                 { return data; }
    public List<double[]> getCenters()              { return centers; }
    public int[]          getTrueLabels()           { return trueLabels; }

    public void setConvergenceThreshold(double convergenceThreshold) { this.convergenceThreshold = convergenceThreshold; }
    public void setFile(File file)                                   { this.file = file; }
    public void setNoOfClusters(int noOfClusters)                    { this.noOfClusters = noOfClusters; }
    public void setNoOfMaxIterations(int noOfMaxIterations)          { this.noOfMaxIterations = noOfMaxIterations; }
    public void setNoOfRuns(int noOfRuns)                            { this.noOfRuns = noOfRuns; }
    public void setNoOfColumns(int noOfColumns)                      { this.noOfColumns = noOfColumns; }
    public void setNoOfRows(int noOfRows)                            { this.noOfRows = noOfRows; }
    public void setData(List<double[]> data)                         { this.data = data; }
    public void setCenters(List<double[]> centers)                   { this.centers = centers; }
    public void setTrueLabels(int[] labels)                          { this.trueLabels = labels; }

    /**
     * Used for displaying data
     */

    public void displayData(List<double[]> data) {
        for (double[] row : data) {
            for (double dataPoint : row) {
                System.out.print(dataPoint + " ");
            }
            System.out.print("\n");
        }
    }

    public void displayPoint(double[] data) {
        for (double dataPoint : data) {
            System.out.print(dataPoint + " ");
        }
        System.out.print("\n");
    }

}
