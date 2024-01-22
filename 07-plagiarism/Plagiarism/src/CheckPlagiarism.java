import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckPlagiarism {

    private static double[] compareLines(List<String> parsedContentsA, List<String> parsedContentsB) {
        if (parsedContentsA.isEmpty()) { // if the biggest file is empty, the smallest will also be
            return new double[]{0, 0}; // therefore, they are equal.
        }

        HammingCleared hammingComparator = new HammingCleared();
        int countIdenticalLines = 0;
        double totalDifference = 0;
        // "In order to detect them, start with the file having more lines..."
        for (String lineA : parsedContentsA) {
            int minDifference = Integer.MAX_VALUE;
            if (!lineA.isEmpty()) {
                // "...and compare any line from the first file, to all the lines of the second file..."
                for (String lineB : parsedContentsB) {
                    if (!lineB.isEmpty()) {
                        int distance = hammingComparator.compare(lineA, lineB);
                        countIdenticalLines++;
                        minDifference = Math.min(minDifference, distance);
                    }
                }
                // "...recording the minimal Hamming Distance."
                totalDifference += minDifference;
            }
        }
        return new double[] {totalDifference / (double) parsedContentsA.size(), countIdenticalLines};
    }

    public static double[] compareFiles(String[] filePaths) throws IOException {
        List<String> parsedContentsA = parseContents(filePaths[0]);
        List<String> parsedContentsB = parseContents(filePaths[1]);

        if (parsedContentsB.size() > parsedContentsA.size()) {
            var temp = parsedContentsA;
            parsedContentsA = parsedContentsB;
            parsedContentsB = temp;
        }

        return compareLines(parsedContentsA, parsedContentsB);
    }

    private static List<String> parseContents(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    public static void main(String[] args) throws IOException {
        String workingDirectoryPath = System.getProperty("user.dir");
        File workingDirectory = new File(workingDirectoryPath);
        File[] filesToCheck = workingDirectory.listFiles();
        assert filesToCheck != null;
        int length = filesToCheck.length - 1;
        int detectionThreshold = 10;

        ArrayList<File[]> plagiarizedFiles = new ArrayList<File[]>();
        ArrayList<double[]> plagiarizedFilesReports = new ArrayList<double[]>();

        // for each file
        for (int j = 0; j < length; j++) {
            // we compare it to each other one (except those to which it has already been compared, hence why k = j + 1)
            for (int k = j + 1; k < length; k++) {
                String[] pathsToFiles = new String[]{filesToCheck[j].getAbsolutePath(), filesToCheck[k].getAbsolutePath()};

                double[] comparisonReport = compareFiles(pathsToFiles);
                double averageDifference = comparisonReport[0];

                if (averageDifference <= detectionThreshold) {
                    plagiarizedFiles.add(new File[]{filesToCheck[j], filesToCheck[k]});
                    plagiarizedFilesReports.add(comparisonReport);
                }
            }
        }

        System.out.println("Detected plagiarism in the following pairs of files:\n");
        for (int i = 0; i < plagiarizedFiles.size(); i++) {
            var plagiarizedPair = plagiarizedFiles.get(i);
            var plagiarismReport = plagiarizedFilesReports.get(i);

            System.out.format("%s AND %s\n", plagiarizedPair[0].getName(), plagiarizedPair[1].getName());
            System.out.format("Average difference: %s / Identical lines count: %s\n", plagiarismReport[0], (int) plagiarismReport[1]);
            System.out.println();
        }
    }
}