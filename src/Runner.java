import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Runner {

    /**
     *
     * @param  appObj    - For run information
     * @return runOutPut - Used for the exporter
     *
     * Will go through the number of runs, it will store which run had the best SSE
     * and will return the string object runOutPut for exporting and displaying
     */
    public static List<String> runner(AppObj appObj) {
        int runs = appObj.getNoOfRuns();           // Get the number of runs
        double bestSSE = Double.POSITIVE_INFINITY; // Use Double.POSITIVE_INFINITY so that whatever value is given will always be smaller and replace it
        int bestRun = -1;

        List<String> runOutput = new ArrayList<>();

        long baseSeed = 42L; // Used for random generating

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
