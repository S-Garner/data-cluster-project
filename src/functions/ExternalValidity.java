package functions;
/**
 *   CSCI 4372: Phase 5: External Validation
 *      Author: Seth Garner
 *        Date: <2025-11-20 Thu>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                  * This is used for external validation and finding true cluster labels using 
 *              }
 */


public class ExternalValidity {

    public static class PairCounts {
        public final long a; 
        public final long b; 
        public final long c; 
        public final long d; 

        /**
         * Constructor for PairCounts, utilized to maintain 
         */
        public PairCounts(long a, long b, long c, long d) {
            this.a = a; // Same in truth and same in predicted Correct positive
            this.b = b; // Same in truth and diff in predicted False negative   (Split)
            this.c = c; // Diff in truth and same in predicted False positive   (Merged Cluster)
            this.d = d; // Diff in truth and diff in predicted Correct Negative (Both agree seperate)
        }

        public long totalPairs() {
            return a + b + c + d;
        }
    }

    public static PairCounts computePairCounts(int[] truth, int[] predicted) {
        if (truth == null || predicted == null) {
            throw new IllegalArgumentException("Label arrays must not be null.");
        }
        if (truth.length != predicted.length) {
            throw new IllegalArgumentException(
                "Label arrays must have the same length: truth = "
                + truth.length + ", predicted = " + predicted.length
            );
        }

        int n = truth.length;
        long a = 0L;
        long b = 0L;
        long c = 0L;
        long d = 0L;

        for (int i = 0; i < n; i++) {
            int trueIndex      = truth[i];
            int predictedIndex = predicted[i];

            for (int j = i + 1; j < n; j++) {
                boolean sameTruth = (trueIndex == truth[j]);
                boolean samePred  = (predictedIndex == predicted[j]);

                if (sameTruth && samePred) {
                    a++;
                } else if (sameTruth) {
                    b++;
                } else if (samePred) {
                    c++;
                } else {
                    d++;
                }
            }
        }

        return new PairCounts(a, b, c, d);
    }

    public static double randIndex(int[] truth, int[] predicted) {
        PairCounts pairs = computePairCounts(truth, predicted);
        double total = (double) pairs.totalPairs();

        if (total == 0.0) {
            return 0.0;
        }
        
        return (pairs.a + pairs.d) / total;
    }

    public static double jaccardIndex(int[] truth, int[] predicted) {
        PairCounts pairs = computePairCounts(truth, predicted);
        double denomenator = (double) (pairs.a + pairs.b + pairs.c);

        if (denomenator == 0.0) {
            return 0.0;
        }

        return pairs.a / denomenator;
    }

    public static double fowlkesMallowsIndex(int[] truth, int[] predicted) {
        PairCounts pairs = computePairCounts(truth, predicted);

        double denominator = MathFuncs.sqrt(
            (double) (pairs.a + pairs.b) * (double) (pairs.a + pairs.c)
        );

        if (denominator == 0.0) {
            return 0.0;
        }

        return pairs.a / denominator;
    }
}