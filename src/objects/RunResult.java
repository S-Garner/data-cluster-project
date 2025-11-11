/**
 *   CSCI 4372: Phase 3: Initiatilization and Running
 *      Author: Seth Garner
 *        Date: <2025-10-18 >
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Will hold the result of a single run
 *              }
 */

package objects;

import java.util.List;

public class RunResult {
    private double initialSSE;
    private double finalSSE;
    private int iterations;
    private int runNumber;

    private List<double[]> finalCenters;
    private int[] finalAssign;
    private int N, D;

    /*
     * 
     */
    public void setFinalCenters(List<double []> centers) {
        this.finalCenters = centers;
    }

    public List<double []> getFinalCenters() {
        return finalCenters;
    }

    public void setFinalAssign(int[] assigns) {
        this.finalAssign = assigns;
    }

    public int[] getFinalAssign() {
        return finalAssign;
    }

    public void setShape(int n, int d) {
        this.N = n;
        this.D = d;
    }

    public int getN() {
        return N;
    }

    public int getD() {
        return D;
    }

    /*
     * Constructor
     */
    public RunResult() {
        this.initialSSE = 0.0;
        this.finalSSE = 0.0;
        this.iterations = 0;
        this.runNumber = 0;
    }

    /*
     * Constructor with parameters
     */
    public RunResult(double initialSSE, double finalSSE, int iterations) {
        this.initialSSE = initialSSE;
        this.finalSSE = finalSSE;
        this.iterations = iterations;
    }

    /*
     * Setters and Getters
     */
    public void setInitialSSE(double initialSSE) {
        this.initialSSE = initialSSE;
    }

    public void setFinalSSE(double finalSSE) {
        this.finalSSE = finalSSE;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void setRunNumber(int runNumber) {
        this.runNumber = runNumber;
    }

    public double getInitialSSE() { 
        return initialSSE; 
    }

    public double getFinalSSE() { 
        return finalSSE; 
    }
    
    public int getIterations() { 
        return iterations; 
    }

    public int getRunNumber() {
        return runNumber;
    }

}
