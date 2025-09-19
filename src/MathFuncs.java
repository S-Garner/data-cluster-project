import java.util.List;

public class MathFuncs {

    public static double getPointDistance(double point, double center) {
        double distance = point - center;
        return distance * distance;
    }

    public static double getCenterDistance(double[] center, double[] points) {
        double distance = 0.0;

        for (int i = 0; i < center.length; i++) {
            distance += getPointDistance(points[i], center[i]);
        }

        return distance;
    }

}
