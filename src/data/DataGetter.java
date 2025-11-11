/**
 *   CSCI 4372: Phase 2: Data Handling
 *      Author: Seth Garner
 *        Date: <2025-10-17 Tue>
 * Style Guide: [[https:https://www.cs.cornell.edu/courses/JavaAndDS/JavaStyle.html][Cornell University Java Code Style Guidelines]]
 * Description: {
 *                 * Will get the data from the file, set rows/cols
 *                 * Get random number in range
 *              }
 */

package data;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import objects.AppObj;

public class DataGetter {

    public static void setRowsAndColumns(AppObj appObj) {

        Scanner scanner;

        try
        {
            scanner = new Scanner(appObj.getFile());

            appObj.setNoOfRows(scanner.nextInt());
            appObj.setNoOfColumns(scanner.nextInt());

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public static List<double[]> getFullDataSet(AppObj appObj) {
        List<String> dataLines = new ArrayList<>();

        try (Scanner scanner = new Scanner(appObj.getFile())) {
            // Skip header line with rows/cols
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Collect all data lines
            while (scanner.hasNextLine()) {
                dataLines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found\n");
        }

        List<double[]> outData = new ArrayList<>(dataLines.size());
        int lineNo = 0;

        for (String line : dataLines) {
            lineNo++;
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            double[] lineArray = new double[appObj.getNoOfColumns()];

            try (Scanner scan = new Scanner(line)) {
                int j = 0;

                while (j < appObj.getNoOfColumns() && scan.hasNext()) {
                    if (!scan.hasNextDouble()) {
                        throw new IllegalArgumentException("Non-numeric token on line " + lineNo + ": " + line);
                    }
                    lineArray[j++] = scan.nextDouble();
                }

                if (j != appObj.getNoOfColumns()) {
                    throw new IllegalArgumentException("Error: Expected " + appObj.getNoOfColumns() + " values, got " + j + " on line " + lineNo);
                }

                if (scan.hasNext()) {
                    throw new IllegalArgumentException("Extra values after " + appObj.getNoOfColumns() + " values on line " + lineNo + ": " + line);
                }
            }
            outData.add(lineArray);
        }

        return outData;
    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static List<double[]> getCenters(AppObj appObj) {

        List<Integer> indices = new ArrayList<>(appObj.getNoOfRows());

        for (int i = 0; i < appObj.getNoOfRows(); i++)
        {
            indices.add(i);
        }

        Collections.shuffle(indices, new Random(System.nanoTime()));

        List<double[]> centers = new ArrayList<>(appObj.getNoOfClusters());

        for (int i = 0; i < appObj.getNoOfClusters(); i++) {
            centers.add(appObj.getData().get(indices.get(i)));
        }

        return centers;
    }

    /*
     * Description: * Will create random partitions of the data for initial centers
     * @param appObj
     * @return List<double[]> - The initial centers
     */
    public static List<double[]> randomPartitions(AppObj appObj) {
        List<double[]> data = appObj.getData();

        int numOfRows = appObj.getNoOfRows(); 
        int numOfClusters = appObj.getNoOfClusters();
        int numOfColumns = appObj.getNoOfColumns();

        Random random = new Random();

        List<List<double[]>> clusters = new ArrayList<>(numOfClusters);

        for (int i = 0; i < numOfClusters; i++) { // Initialize clusters
            clusters.add(new ArrayList<>());      // Add empty cluster
        }

        for (int i = 0; i < numOfRows; i++) {               // Assign data points to random clusters
            int clusterId = random.nextInt(numOfClusters);  // Get random cluster id
            clusters.get(clusterId).add(data.get(i));       // Add data point to cluster
        }

        for (int i = 0; i < numOfClusters; i++) {           // Ensure no cluster is empty
            if (clusters.get(i).isEmpty()) {                // If cluster is empty
                for (int j = 0; j < numOfClusters; j++) {   // Find a cluster to steal from
                    if (clusters.get(j).size() > 1) {       // If cluster has more than 1 point
                        double[] temp = clusters.get(j).remove(random.nextInt(clusters.get(j).size())); // Remove random point
                        clusters.get(i).add(temp);          // Add to empty cluster
                        break;
                    }
                }
            }
        }

        List<double[]> centers = new ArrayList<>(numOfClusters);
        for (List<double[]> cluster : clusters) {
            double[] center = new double[numOfColumns];
            if (cluster.isEmpty()) {
                continue;
            }

            for (double[] point : cluster) {
                for (int j = 0; j < numOfColumns; j++) {
                    center[j] += point[j];
                }
            }

            for (int j = 0; j < numOfColumns; j++) {
                center[j] /= cluster.size();
            }
            centers.add(center);
        }

        return centers;
    }

    public static List<double[]> normalize(List<double[]> data) {
        if (data.isEmpty()) {
            System.err.println("Error: Data is empty, could not normalize");
            return data;
        }

        int numOfColumns = data.get(0).length;

        double[] min = new double[numOfColumns];
        double[] max = new double[numOfColumns];

        Arrays.fill(min, Double.POSITIVE_INFINITY);
        Arrays.fill(max, Double.NEGATIVE_INFINITY);

        for (double[] row : data){
            for (int i = 0; i < numOfColumns; i++) {
                double val = row[i];
                
                if (val < min[i]) {
                    min[i] = val;
                }

                if (val > max[i]) {
                    max[i] = val;
                }
            }
        }

        List<double[]> normalizedData = new ArrayList<>(data.size());

        for (double[] row : data) {
            double[] normalizedRow = new double[numOfColumns];

            for (int i = 0; i < numOfColumns; i++) {
                double range = max[i] - min[i];

                if (range == 0.0) {
                    normalizedRow[i] = 0.0;
                }
                else {
                    normalizedRow[i] = (row[i] - min[i]) / range;
                }
            }
            normalizedData.add(normalizedRow);
        }

        return normalizedData;
    }

}
