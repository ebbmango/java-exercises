import java.util.Random;

public class Utilities {

    public static boolean rollTheDice(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Invalid input: Probability must be between 0.");
        }

        double randomValue = Math.random();
        double probabilityThreshold = probability * 100;

        return randomValue <= probabilityThreshold;
    }

    public static void shuffleArray(boolean[][] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            // Generate a random index between 0 and i (inclusive)
            int randomIndex = random.nextInt(i + 1);
            // Swap the current element with the randomly selected element
            boolean[] temp = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = temp;
        }
    }
}