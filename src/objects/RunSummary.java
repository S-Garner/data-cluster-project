/**
 *   CSCI 4372: Phase 3: Initiatilization and Running
 *      Author: Seth Garner
 *        Date: <2025-10-18>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * Will hold the summary of a run
 *              }
 */

package objects;
import java.util.List;

public class RunSummary {
    private RunResult result;
    private List<String> runOutput;
    private int bestRunNumber;

    /*
     * Constructor
     */
    public RunSummary() {
        this.result = null;
        this.runOutput = null;
        this.bestRunNumber = 0;
    }

    /*
     * Constructor with parameters
     */
    public RunSummary(RunResult result, List<String> runOutput, int bestRunNumber) {
        this.result = result;
        this.runOutput = runOutput;
        this.bestRunNumber = bestRunNumber;
    }

    /*
     * Setters and Getters
     */
    public RunResult getResult() {
        return result;
    }

    public List<String> getRunOutput() {
        return runOutput;
    }

    public void setResult(RunResult result) {
        this.result = result;
    }

    public void setRunOutput(List<String> runOutput) {
        this.runOutput = runOutput;
    }

    public void setBestRunNumber(int number) {
        this.bestRunNumber = number;
    }
}