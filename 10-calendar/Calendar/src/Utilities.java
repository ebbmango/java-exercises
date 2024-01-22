public class Utilities {
    public static String ordinalize(int number) {
        int lastDigit = number % 10;
        switch (lastDigit) {
            case 1:
                return number + "st";
            case 2:
                return number + "nd";
            case 3:
                return number + "rd";
            default:
                return number + "th";
        }
    }
}
