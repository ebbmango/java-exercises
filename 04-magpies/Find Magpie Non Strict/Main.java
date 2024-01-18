import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static BufferedImage LoadImage(String path) {
        File imageFile = new File(path);
        if (!imageFile.exists()) {
            throw new IllegalArgumentException("File not found: " + path);
        }
        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {

            throw new RuntimeException("Error reading the image: " + e.getMessage());
        }
    }

    private static int[][] ImageToArray(BufferedImage bufferedImage) {

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[][] binaryArray = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = bufferedImage.getRGB(j, i);

                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                int[] RGB = { red, green, blue };
                Arrays.sort(RGB);
                int luminosity = (RGB[0] + RGB[RGB.length - 1]) / 2;

                if (luminosity < 128) {
                    binaryArray[i][j] = 0;
                } else {
                    binaryArray[i][j] = 1;
                }
            }
        }
        return binaryArray;
    }

    private static int[][] TransposeMatrix(int[][] matrix) {
        int height = matrix.length;
        int width = matrix[0].length;
        int[][] transposedMatrix = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                transposedMatrix[i][j] = matrix[j][i];
            }
        }
        return transposedMatrix;
    }

    private static boolean IsAllEmpty(int[] binaryArray) {
        for (int number : binaryArray)
            if (number != 0)
                return false;
        return true;
    }

    private static int[] MeasureBorders(int[][] referenceImageArray) {
        int height = referenceImageArray.length;
        int width = referenceImageArray[0].length;

        int blankRowsTop = 0;
        while (blankRowsTop < height && IsAllEmpty(referenceImageArray[blankRowsTop])) {
            blankRowsTop++;
        }

        int blankRowsBottom = height - 1;
        while (blankRowsBottom > 0 && IsAllEmpty(referenceImageArray[blankRowsBottom])) {
            blankRowsBottom--;
        }
        blankRowsBottom = height - (blankRowsBottom + 1);

        int[][] transposedReference = TransposeMatrix(referenceImageArray);

        int blankRowsLeft = 0;
        while (blankRowsLeft < width && IsAllEmpty(transposedReference[blankRowsLeft])) {
            blankRowsLeft++;
        }

        int blankRowsRight = width - 1;
        while (blankRowsRight > 0 && IsAllEmpty(transposedReference[blankRowsRight])) {
            blankRowsRight--;
        }
        blankRowsRight = width - (blankRowsRight + 1);

        return new int[] { blankRowsTop, blankRowsBottom, blankRowsLeft, blankRowsRight };
    }

    private static int[][] CropReferenceImage(int[][] referenceImageArray) {
        int height = referenceImageArray.length;
        int width = referenceImageArray[0].length;
        int[] borders = MeasureBorders(referenceImageArray);

        int newHeight = height - borders[0] - borders[1];
        int newWidth = width - borders[2] - borders[3];

        int[][] Xcropped = new int[newWidth][height];
        System.arraycopy(TransposeMatrix(referenceImageArray), borders[2], Xcropped, 0, newWidth);
        int[][] Ycropped = new int[newHeight][newWidth];
        System.arraycopy(TransposeMatrix(Xcropped), borders[0], Ycropped, 0, newHeight);

        return Ycropped;
    }

    private static String ArrayToString(int[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int element : array) {
            stringBuilder.append(element);
        }
        return stringBuilder.toString();
    }

    private static String MagpiesForehead(int[][] magpieMatrix) {

        int[] firstRow = magpieMatrix[0];

        int cutLengthTail = firstRow.length;
        while (firstRow[cutLengthTail - 1] == 0) {
            --cutLengthTail;
        }

        int cutLengthHead = 0;
        while (firstRow[cutLengthHead] == 0) {
            ++cutLengthHead;
        }

        int[] relevantPattern = Arrays.copyOfRange(firstRow, cutLengthHead, cutLengthTail);

        int[] relevantArray = new int[relevantPattern.length + 2];
        relevantArray[0] = 0;
        System.arraycopy(relevantPattern, 0, relevantArray, 1, relevantPattern.length);
        relevantArray[relevantArray.length - 1] = 0;

        return ArrayToString(relevantArray);
    }

    public static int[] ListToArray(ArrayList<Integer> arrayList) {
        Integer[] array = arrayList.toArray(new Integer[0]);

        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = array[i];
        }

        return intArray;
    }

    public static int[] FindAllSubstringOccurrences(String mainString, String substring) {
        int index = 0;
        ArrayList<Integer> indicesList = new ArrayList<>();

        while ((index = mainString.indexOf(substring, index)) != -1) {
            indicesList.add(index);
            index += substring.length();
        }

        return ListToArray(indicesList);
    }

    public static int[][] TrimMatrix(int[][] matrix, int leftBound, int rightBound, int topBound, int botBound) {
        int[][] trimmedMatrix = new int[botBound - topBound][rightBound - leftBound];

        for (int i = leftBound; i < rightBound; i++) {
            for (int j = topBound; j < botBound; j++) {
                trimmedMatrix[j - topBound][i - leftBound] = matrix[j][i];
            }
        }

        return trimmedMatrix;
    }

    public static int[][] ExtractMagpie(int[][] birdsArray, int X, int Y) {
        // this was also changed to account for the magpie with the cut tail
        return TrimMatrix(birdsArray, X - 6, X + 51, Y, Y + 39);
    }

    public static ArrayList<int[]> GetMagpies(int[][] birdsArray, int[][] magpieArray) {
        int[][] croppedMagpieArray = CropReferenceImage(magpieArray);

        // since this is the non-strict version, we need to account for only those parts of the magpie binary which are
        // equal for all magpies (including the one with the cut tail)
        int croppedMagpieArrayHeight = croppedMagpieArray.length;
        int croppedMagpieArrayWidth = croppedMagpieArray[0].length;
        int[][] nonStrictCropping = TrimMatrix(croppedMagpieArray, 0, croppedMagpieArrayWidth - 13, 0,
                croppedMagpieArrayHeight);

        String magpiesForehead = MagpiesForehead(croppedMagpieArray);

        ArrayList<int[]> coordinates = new ArrayList<>();
        for (int i = 0; i < birdsArray.length; i++) {
            int[] occurrences = FindAllSubstringOccurrences(ArrayToString(birdsArray[i]), magpiesForehead);

            for (int occurrence : occurrences) {
                int[] coordinate = { i, occurrence };
                coordinates.add(coordinate);
            }
        }

        ArrayList<int[]> magpiesCoordinates = new ArrayList<>();

        for (int[] coordinate : coordinates) {

            int[][] possibleMagpie = ExtractMagpie(birdsArray, coordinate[1], coordinate[0]);

            boolean isMagpie = true;
            for (int i = 0; i < possibleMagpie.length; i++) {
                if (!Arrays.equals(possibleMagpie[i], nonStrictCropping[i])) {
                    isMagpie = false;
                    break;
                }
            }
            if (isMagpie) {
                magpiesCoordinates.add(coordinate);
            }
        }
        return magpiesCoordinates;
    }

    public static int[][] ClearImage(int[][] birdsArray, ArrayList<int[]> magpiesCoordinates) {
        int height = birdsArray.length;
        int width = birdsArray[0].length;
        int[][] blankArray = new int[height][width];

        for (int[] coordinate : magpiesCoordinates) {
            int X = coordinate[1];
            int Y = coordinate[0];
            for (int i = Y; i < Y + 39; i++) {

                if (X + 64 - (X - 6) >= 0) {
                    System.arraycopy(birdsArray[i], X - 6, blankArray[i], X - 6, X + 64 - (X - 6));
                }
            }
        }
        return blankArray;
    }

    public static void DisplayImage(int[][] onlyMagpiesBinary) {

        BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_BYTE_GRAY);
        Color white = new Color(255, 255, 255);
        int whiteRGB = white.getRGB();

        int height = onlyMagpiesBinary.length;
        int width = onlyMagpiesBinary[0].length;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (onlyMagpiesBinary[i][j] != 0) {
                    image.setRGB(j, i, whiteRGB);
                }
            }
        }

        try {
            File outputTiffFile = new File("output.tiff");
            ImageIO.write(image, "tiff", outputTiffFile);
        } catch (IOException e) {
            System.out.println("Error in creating image:\n" + e);
        }
    }

    public static void main(String[] args) {
        BufferedImage magpieImage = LoadImage("./magpie.tif");
        BufferedImage birdsImage = LoadImage("./birds.tif");
        int[][] magpieArray = ImageToArray(magpieImage);
        int[][] birdsArray = ImageToArray(birdsImage);

        ArrayList<int[]> magpiesCoordinates = GetMagpies(birdsArray, magpieArray);
        int[][] clearedArray = ClearImage(birdsArray, magpiesCoordinates);
        DisplayImage(clearedArray);
    }
}