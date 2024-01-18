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
        } catch (
                IOException e) { // This is quite interesting: exception objects should also be imported and used from external libraries
            throw new RuntimeException("Error reading the image: " + e.getMessage());
        }
    }

    private static int[][] ImageToArray(BufferedImage bufferedImage) {
        // The plan now is to create a binary array to represent image, pixel by pixel, with numerical values.
        // For that it would be convenient for our array to be two-dimensional (that is, a matrix), for in such a way
        // ... we can more intuitively organize the data concerning each pixel in the image in rows and columns.
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[][] binaryArray = new int[height][width];

        // Now we have to loop through each and every single pixel of the image and store its value in the array.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = bufferedImage.getRGB(j, i);
                // Running this line of code, we get two possible integer values from a purely black and white image:
                // -16777216 and -1. Let's figure out why this is the case and what those values mean.
                // The .getRGB() method from the BufferedImage class returns not an integer, but a TYPE_INT_ARGB value
                // (https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#TYPE_INT_ARGB), whose
                // description in the documentation reads as follows: "Represents an image with 8-bit RGBA color
                // components packed into integer pixels." The 8-bit RGBA color scheme works by storing the information
                // concerning the represented color in 8 hexadecimal bits, with each pair of bits representing each of
                // the color channels that compose it: one for red (R), one for green (G), one for blue (B) and one for
                // the opacity, called alpha (A).
                // Now, in our specific case, the color scheme is specifically ARGB, which means that taking apart a
                // hexadecimal number into its composing pairs of bits, each of them would, from left to right, store
                // the values for the color channels in the order that they appear in the acronym: first alpha, then
                // red, then green, and finally blue (https://en.wikipedia.org/wiki/RGBA_color_model#ARGB32).
                // Let's take, for example, a value such as 80FFFF00; what colors does it represent? To begin with, we
                // can separate it into its channels: A = 80, R = FF, G = FF, B = 00. Since the red and green channels
                // are both maxed out whilst the blue one is completely empty, we know for a fact that the color is
                // yellow. All that is left now is to figure out its opacity. Since we are dealing with hexadecimals,
                // and since the largest value that we can achieve with two hexadecimal digits is FF (which converts to
                // 255 in the decimal number system), we know that we are not dealing with a 0-to-100 scale, but rather
                // with a 0-to-255 one. Since 80 in hexadecimal corresponds to 128 in decimal, we know that our color's
                // opacity is that of 128 out of 255, which is approximately 50.2%.
                // Perfect, now that we know how this works, we can go back to what we can expect from the result of
                // the bufferedImage.getRGB(j, i) command. Building and running this code to extract the values from a
                // purely black-and-white image, I got two values: -1 and -16777216. This is because Java is printing
                // the result in the decimal system. Converting these values to a hexadecimal numbering system using
                // the piece of code below...
                // System.out.println(String.format("0x%08X", pixel));
                // ...the values became 0xFFFFFFFF and 0xFF000000 instead, which reveals that the method Java is using
                // to make the conversions between hexadecimals and decimals is the signed 2's complement.
                // For reference, please check the following links:
                // https://en.wikipedia.org/wiki/Two%27s_complement
                // https://www.rapidtables.com/convert/number/hex-to-decimal.html

                // This should be enough for our purposes if the image is itself purely black-and-white (please note:
                // PURELY black and white – that is, binary –, and NOT in grayscale!). However, since we do not expect
                // the user to input solely and purely black-and-white images, we should work on making this conversion
                // ourselves. We do it in the following manner:

                // int alpha = (pixel>>24) & 0xff; (OBS: Unused, therefore commented out).
                int red   = (pixel >> 16) & 0xff;
                int green = (pixel >> 8)  & 0xff;
                int blue  = pixel & 0xff;

                // At first, the code just above might make little to no sense at all. Let us take some time to figure
                // out how exactly does it work and what exactly it is doing.
                //
                // -*-*-*-*- 1) BITWISE "SHIFT" -*-*-*-*-
                // First, we are using the "bitwise right shift operator" (>>). This operator shifts all the digits in
                // a BINARY number from the left to the right, filling the space(s) left empty on the left with zeroes
                // and doing away with the rightmost digit(s) in case they are "shifted out" of the number.
                // Example:
                // 24 >> 1
                // 24 in binary = 11000
                // amount of shifts = 1
                // => 24 >> 1 = 0b11000 >> 1 = 0b01100 = 0b1100 = 12
                // Another example:
                // 38 >> 2
                // 38 in binary = 100110
                // amount of shifts = 2
                // => 38 >> 2 = 0b100110 >> 2 = 0b010011 >> 1 = 0b001001 = 0b1001 = 9
                // (OBS: In practice, ">> 1" is tantamount to a division by two (rounded down in case of odd numbers)
                //
                // -*-*-*-*- 2) BITWISE "AND" -*-*-*-*-
                // This one is even easier to understand. Given two binary numbers, we superpose their digits. If both
                // digits (from both binary numbers) are one, the corresponding digit in the resulting binary number
                // will also be one, else, it will be zero.
                // Example:
                // 5 & 6
                // 5 in binary = 101
                // 6 in binary = 110
                // => 5 & 6 = ...
                // 1 0 1
                // 1 1 0
                // - - -
                // 1 0 0
                // ... => therefore, 5 & 6 = 0b101 & 0b110 = 0b100 = 4
                //
                // -*-*-*-*- 3) THE WHOLE OPERATION -*-*-*-*-
                // Let us start with the first line of code: int alpha = (pixel>>24) & 0xff;
                // First we perform 24 shifts in the binary form of the pixel value. Let us take Whatsapp's light green
                // color as an example: #FF25D366 (in Java notation, 0xFF25D366). In binary, such color is:
                // 0xFF25D366 = 11111111001001011101001101100110, which consists of 32 digits in total.
                // This means that if we had two digits for each channel in hexadecimal, in binary, we have eight.
                // 0x FF 25 D3 66 = 0b 11111111 00100101 11010011 01100110 => ...
                // (0x) FF = (0b) 11111111
                // (0x) 25 = (0b) 00100101
                // (0x) D3 = (0b) 11010011
                // (0x) 66 = (0b) 01100110
                // We perform 24 right shifts in this number:
                // 0b 11111111 00100101 11010011 01100110 >> 24 = 0b 00000000 00000000 00000000 11111111
                // Or, at least, this is what we would expect. What we actually get from this operation is:
                // 0b 11111111 11111111 11111111 11111111. Why is that?
                // Well, the reason for that is because, as said previously, to perform conversions between numbering
                // systems, Java uses the SIGNED 2's complement method. This method dictates that the leftmost digit of
                // a number represented in binary should be used to represent its sign. This is useful because it allows
                // for the representation of negative numbers as well as for that of positive ones, and since Java deals
                // with signed integers, its developers opted for using a method capable of dealing with them.
                // Therefore, what is happening here is that we are performing a shift and, since the resulting number
                // is negative, instead of the leftmost spot left empty being filled with a zero (as it had been before,
                // in the examples given above), it is being filled with one, to account for its negative sign. Since
                // the ">>" operations are being chained (24 times in a row), we are also chain-appending ones instead
                // of zeroes to the left-hand side of the number. So our final result will be
                // 0b 11111111 11111111 11111111 11111111 instead of 0b 00000000 00000000 00000000 11111111.
                // Finally, we perform a bitwise AND operation between this number and 0xff (0b11111111):
                //   | 1 1 1 1 1 1 1 1   1 1 1 1 1 1 1 1   1 1 1 1 1 1 1 1   1 1 1 1 1 1 1 1 |
                // & | 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   1 1 1 1 1 1 1 1 |
                //   | - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
                // = | 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   1 1 1 1 1 1 1 1 |
                // OBS: As you can see, since 0xff consists, in binary, of only 8 digits, when we perform a bitwise AND
                // operation with it, all the extra digits (in case there are any) of the number we operate it with are
                // treated as if being paired with zeroes. Since the bitwise AND operator only returns 1 when the
                // corresponding digits of both numbers are 1, performing the bitwise AND operation between any number
                // in hexadecimal and 0xff is effectively a clever way of getting rid of all digits except for the very
                // last eight ones.
                // In summary, by performing bitwise SHIFT operations, we are moving the eight digits corresponding to
                // each color channel to the right spot for them to be extracted by their subsequent digit-by-digit
                // comparison with 0xff by means of the bitwise AND operator. It is no surprise, therefore, that our
                // result is 11111111: we merely extracted the value of the alpha channel from the hex color code, which
                // was 0xFF (in binary: 0b11111111; in decimal: 255).
                // Now that the logic has been unfolded and thoroughly explained, we understand what the above lines of
                // code are doing: extracting the values of each channel from the color's numerical representation, and
                // storing each channel's value in a dedicated variable with its channel's name.

                // Now, what we were tasked with doing is representing the image binarily: if the pixel is considered to
                // be bright, we represent it as being purely white; if it is considered to be dark, we represent it as
                // being purely black.
                // Doing so is easy enough if when dealing with an RGB color scheme: we can simply convert it to HSL and
                // extract its brightness by means of the formula:
                // Luminosity = (Cmin + Cmax) / 2
                // Where:
                // Cmin = the smallest decimal value found across the R, G and B channels (divided by 255)
                // Cmax = the largest decimal value found across the R, G and B channels (divided by 255).
                // It is true, however, that we are not dealing with an RGB, but rather with an RGBA color scheme.
                // Nevertheless, our strategy shall not change one bit, for the brightness of the pixel with an alpha
                // channel not completely filled will depend on the color of the background atop of which the image is
                // displayed, and since for that there is no standard of any sort, there is no way of involving the
                // alpha channel in the making of calculations.
                // We proceed, therefore, by taking the minimum and maximum values out of the color channels and
                // finding the resulting brightness of the pixel through means of them.

                int[] RGB = {red, green, blue};
                Arrays.sort(RGB);
                int luminosity = (RGB[0] + RGB[RGB.length - 1]) / 2;

                if (luminosity < 128) {
                    binaryArray[i][j] = 0; // (dark pixel)
                } else {
                    binaryArray[i][j] = 1; // (bright pixel)
                }
            }
        }
        return binaryArray;
    }

    // "Prints" the image in the terminal (CAUTION: Use with very small images only!)
//    private static void PrintRepresentation(int[][] imageArray) {
//        for (int[] row : imageArray) {
//            for (int pixel : row) {
//                System.out.print(" " + (pixel > 0 ? 0 : " "));
//            }
//            System.out.println();
//        }
//    }

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
        for (int number : binaryArray) if (number != 0) return false;
        return true;
    }

    private static int[] MeasureBorders(int[][] referenceImageArray) {
        int height = referenceImageArray.length;
        int width = referenceImageArray[0].length;

        // Counting the amount of empty rows from top to bottom
        int blankRowsTop = 0;
        while (blankRowsTop < height && IsAllEmpty(referenceImageArray[blankRowsTop])) {
            blankRowsTop++;
        }

        // Counting the amount of empty rows from the bottom up
        int blankRowsBottom = height - 1;
        while (blankRowsBottom > 0 && IsAllEmpty(referenceImageArray[blankRowsBottom])) {
            blankRowsBottom--;
        }
        blankRowsBottom = height - (blankRowsBottom + 1);

        // Transposing the matrix to make it easier to count empty columns from left to right and from right to left
        int[][] transposedReference = TransposeMatrix(referenceImageArray);

        // Counting the amount of empty columns from left to right
        int blankRowsLeft = 0;
        while (blankRowsLeft < width && IsAllEmpty(transposedReference[blankRowsLeft])) {
            blankRowsLeft++;
        }

        // Counting the amount of empty rows from the bottom up
        int blankRowsRight = width - 1;
        while (blankRowsRight > 0 && IsAllEmpty(transposedReference[blankRowsRight])) {
            blankRowsRight--;
        }
        blankRowsRight = width - (blankRowsRight + 1);

        return new int[]{blankRowsTop, blankRowsBottom, blankRowsLeft, blankRowsRight};
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
        for (int element : array) { stringBuilder.append(element); }
        return stringBuilder.toString();
    }

    // This method extracts the relevant pattern (only values bounded by ones) in the first line of the binary matrix
    private static String MagpiesForehead(int[][] magpieMatrix) {
        // OBS: this method CANNOT be used (for it will result in an infinite loop) if the first row is fully composed of zeroes!
        int[] firstRow = magpieMatrix[0];

        // first we get the amount of trailing zeroes
        int cutLengthTail = firstRow.length;
        while (firstRow[cutLengthTail-1] == 0) {
            --cutLengthTail;
        }

        // then we get the amount of leading zeroes
        int cutLengthHead = 0;
        while (firstRow[cutLengthHead] == 0) {
            ++cutLengthHead;
        }
        // then we slice the array to remove them all
        int[] relevantPattern = Arrays.copyOfRange(firstRow, cutLengthHead, cutLengthTail);

        // now we create another array with ONE leading and ONE trailing zero
        int[] relevantArray = new int[relevantPattern.length + 2];
        relevantArray[0] = 0;
        System.arraycopy(relevantPattern, 0, relevantArray, 1, relevantPattern.length);
        relevantArray[relevantArray.length - 1] = 0;

        return ArrayToString(relevantArray);
    }

    // converts an ArrayList of Integers to an array of integers
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

    public static int[][] TrimMatrix (int[][] matrix, int leftBound, int rightBound, int topBound, int botBound) {
        int[][] trimmedMatrix = new int[botBound - topBound][rightBound - leftBound];

        for (int i = leftBound; i < rightBound; i++) {
            for (int j = topBound; j < botBound; j++) {
                trimmedMatrix[j - topBound][i - leftBound] = matrix[j][i];
            }
        }

        return trimmedMatrix;
    }

    public static int[][] ExtractMagpie (int[][] birdsArray, int X, int Y) {
        return TrimMatrix(birdsArray, X - 6, X + 64, Y, Y + 39);
    }

    public static ArrayList<int[]> GetMagpies(int[][] birdsArray, int[][] magpieArray) {
        int[][] croppedMagpieArray = CropReferenceImage(magpieArray);
        // here we are extracting the relevant pattern of the first line of the binary
        String magpiesForehead = MagpiesForehead(croppedMagpieArray);

        // Now we are finding all the points (Y and X coordinates) in the birds matrix in which we find the same pattern as that of a Magpie's forehead
        // (i.e., all places in which a Magpie can possibly be found) // OBS (IMPORTANT!) : This gives us coordinates in the order Y, X
        ArrayList<int[]> coordinates = new ArrayList<>();
        for (int i = 0; i < birdsArray.length; i++) {
            int[] occurrences = FindAllSubstringOccurrences(ArrayToString(birdsArray[i]), magpiesForehead);

            for (int occurrence : occurrences) {
                int[] coordinate = {i, occurrence};
                coordinates.add(coordinate);
            }
        }

        ArrayList<int[]> magpiesCoordinates = new ArrayList<>();
        // And now we are checking each of the found coordinates (possible magpie spots)
        for (int[] coordinate : coordinates) {

            // We trim the possible magpie out of the birds image
            int[][] possibleMagpie = ExtractMagpie(birdsArray, coordinate[1], coordinate[0]);

            // And compare it, line by line, to our cropped magpie binary representation
            boolean isMagpie = true;
            for (int i = 0; i < possibleMagpie.length; i++) {
                // If we find that in some line their binary representations differ...
                if (!Arrays.equals(possibleMagpie[i], croppedMagpieArray[i])) {
                    // We do not count it
                    isMagpie = false;
                    break;
                }
            }

            if (isMagpie) {
                // After that, we add all the ones whose lines are all equal to those of the cropped magpie binary representation
                magpiesCoordinates.add(coordinate);
            } // And now we know the exact locations of all the magpies!
        }

        return magpiesCoordinates;
    }

    public static int[][] ClearImage (int[][] birdsArray, ArrayList<int[]>magpiesCoordinates) {
        int height = birdsArray.length;
        int width = birdsArray[0].length;
        int[][] blankArray = new int[height][width];

        for (int[] coordinate : magpiesCoordinates) {
            int X = coordinate[1];
            int Y = coordinate[0];
            for (int i = Y; i < Y + 39; i++) {
                // here we are copying the relevant parts (those with a magpie) from the birdsArray to the blankArray
                if (X + 64 - (X - 6) >= 0) {
                    System.arraycopy(birdsArray[i], X - 6, blankArray[i], X - 6, X + 64 - (X - 6));
                }
            }
        }
        return blankArray;
    }

    public static void DisplayImage (int[][] onlyMagpiesBinary) {
        // we first create a blank image
        BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_BYTE_GRAY);
        Color white = new Color(255, 255, 255);
        int whiteRGB = white.getRGB();

        int height = onlyMagpiesBinary.length;
        int width = onlyMagpiesBinary[0].length;

        // now, for every pixel that is a part of a magpie
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (onlyMagpiesBinary[i][j] != 0) {
                    // we make it white
                    image.setRGB(j, i, whiteRGB);
                }
            }
        }

        // and now we make an image out of that!
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