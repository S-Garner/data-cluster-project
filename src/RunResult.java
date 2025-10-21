public class RunResult {
    private double initialSSE;
    private double finalSSE;
    private int iterations;
    private int runNumber;

    public RunResult() {
        this.initialSSE = 0.0;
        this.finalSSE = 0.0;
        this.iterations = 0;
        this.runNumber = 0;
    }

    public RunResult(double initialSSE, double finalSSE, int iterations) {
        this.initialSSE = initialSSE;
        this.finalSSE = finalSSE;
        this.iterations = iterations;
    }

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
