/**
 *   CSCI 4372: Phase 3
 *      Author: Seth Garner
 * Style Guide: Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * 
 *              }
 */

package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.DataGetter;
import objects.AppObj;
import objects.RunResult;

public class ValidatorRunner {

    // Container for each K results
    public static class KResult {
        public int K;
        public double calinskiH, silh;
        public int bestRun;
        public double bestFinalSSE;

        /*
         * @param k
         * @param ch
         * @param sw
         * @param bestFinalSSE
         * @param bestRun
         */
        public KResult(int k, double ch, double sw, int bestRun, double bestFinalSSE) {
            this.K = k;
            this.calinskiH = ch;
            this.silh = sw;
            this.bestRun = bestRun;
            this.bestFinalSSE = bestFinalSSE;
        }
    }

    /*
     * @param base
     * @param Kmin
     * @param Kmax
     * @param R
     * 
     * @return List<KResult>
     */
    public static List<KResult> runner(AppObj base, int Kmin, int Kmax, int R) {
        List<KResult> out = new ArrayList<>();

        for (int i = Kmin; i <= Kmax; i++) {
            base.setNoOfClusters(i);

            double bestSSE = Double.POSITIVE_INFINITY;
            RunResult best = null;

            long seed = 987654321L + i * 7919L;

            int currentProgress = i - Kmin + 1;
            int totalProgress = Kmax - Kmin + 1;

            for (int j = 1; j <= R; j++) {
                base.setCenters(DataGetter.randomPartitions(base));
                List<String> sink = new ArrayList<>();

                RunResult runresult = KMeans.runOneDetailed(base, new Random(seed + j), sink);
                runresult.setRunNumber(j);

                if (runresult.getFinalSSE() < bestSSE) {
                    bestSSE = runresult.getFinalSSE();
                    best = runresult;
                }
            }

            // If best is null then no data to compare
            if (best == null) continue;

            // Calculate the calinski harabas and silhouette scores
            double ch = InternalValidity.calinskiHarabas(
                base.getData(),
                best.getFinalAssign(),
                best.getFinalCenters(),
                best.getFinalSSE()
            );

            // Calculate the silhouette score
            double sw = InternalValidity.silhouette(
                base.getData(),
                best.getFinalAssign(),
                i
            );

            out.add(new KResult(i, ch, sw, best.getRunNumber(), best.getFinalSSE()));

            System.out.print("\r" + 
                String.format("%.0f%%", (currentProgress * 100.0 / totalProgress)) + " complete");
            System.out.flush();

        }

        System.out.println();

        return out;
    }
}
