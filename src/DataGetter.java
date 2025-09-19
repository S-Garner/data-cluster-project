import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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

        Collections.shuffle(indices, new Random());

        List<double[]> centers = new ArrayList<>(appObj.getNoOfClusters());

        for (int i = 0; i < appObj.getNoOfClusters(); i++) {
            centers.add(appObj.getData().get(indices.get(i)));
        }

        return centers;
    }

}
