import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Runner {
    public static List<String> runner(AppObj appObj) {
        int runs = appObj.getNoOfRuns();
        double bestSSE = Double.POSITIVE_INFINITY;
        int bestRun = -1;

        List<String> runOutput = new ArrayList<>();

        long baseSeed = 42L;

        for (int i = 1; i <= runs; i++) {
            runOutput.add("Run " + i + "\n");
            runOutput.add("-----\n");
            double sse = KMeans.runOne(appObj, new Random(baseSeed + i), runOutput);
            if (sse < bestSSE) {
                bestSSE = sse;
                bestRun = i;
            }
            runOutput.add("\n");
        }
        runOutput.add("Best Run: " + bestRun + ": SSE = " + bestSSE);

        return runOutput;
    }
}
