import java.util.List;

public class RunSummary {
    private RunResult result;
    private List<String> runOutput;
    private int bestRunNumber;

    public RunSummary() {
        this.result = null;
        this.runOutput = null;
        this.bestRunNumber = 0;
    }

    public RunSummary(RunResult result, List<String> runOutput, int bestRunNumber) {
        this.result = result;
        this.runOutput = runOutput;
        this.bestRunNumber = bestRunNumber;
    }

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