public class Hamming { // "provide a class Hamming..."

    // "...with the public method compare..."
    public int compare (String lineA, String lineB) throws IllegalArgumentException {
        // "...which compares two strings and returns the Hamming distance."
        int diff = 0;

        if (lineA.isEmpty() || lineB.isEmpty()) {
            throw new IllegalArgumentException("Empty string(s)");
        }

        // We find the minimum length between the two strings
        int minimumLength = Math.min(lineA.length(), lineB.length());

        // We iterate through each character position up to the minimum length (to prevent an IndexOutOfBoundsException)
        for (int i = 0; i < minimumLength; i++) {
            // We compare the characters at the current position of each line and increment 'diff' if they differ
            diff += lineA.charAt(i) != lineB.charAt(i) ? 1 : 0;
        }

        // Add the leftover difference to our accumulator
        diff += Math.abs(lineA.length() - lineB.length());

        return diff;
    }
}
