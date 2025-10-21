import java.util.List;

public class RunSummary {
    private RunResult result;
    private List<String> runOutput;

    public RunSummary() {
        this.result = null;
        this.runOutput = null;
    }

    public RunSummary(RunResult result, List<String> runOutput) {
        this.result = result;
        this.runOutput = runOutput;
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
}