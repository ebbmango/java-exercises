import java.util.Scanner;

public class Main {

    // 5) "Create a validation method which accepts the string entered by the user to specify the month
    //     the user wishes to know the number of days for and return true if a valid month or false if it is not."
    private static boolean validateInputMonth(String string) {
        for (var month : MonthOfYear.values()) {
            if (string.toUpperCase().equals(month.name())) {
                return true;
            }
        }
        throw new IllegalArgumentException("Invalid month entered. Please check your spelling and try again.");
    }

    // 6) "Add a method to Main Class to determine the number of the day in a year, for the month and
    //     the day entered by the user. Determine the total number of days in preceding months, using
    //     MonthofYear enum class."
    private static int dayOfYear( int chosenDay, MonthOfYear chosenMonth) {
        MonthOfYear[] months = MonthOfYear.values();
        for (var month : months) {
            chosenDay += (month.getMonthPosition() < chosenMonth.getMonthPosition()) ? month.getNumDays() : 0;
        }
        return chosenDay;
    }

    // 7) "Add a method to Main Class to determine the month and the day, for the number of the day in
    //     a year, entered by the user."
    private static String dateFromDayOfYear(int dayOfYear) {
        if (dayOfYear < 1 || dayOfYear > 365) {
            throw new IllegalArgumentException("Invalid day of the year. Valid days range from 1 to 365.");
        }

        MonthOfYear[] months = MonthOfYear.values();
        int remainingDays = dayOfYear;

        for (var month : months) { // Up to the last month of the year...
            if (remainingDays <= month.getNumDays()) { // If the amount of days in the month encapsulates the remaining days
                return (Utilities.ordinalize(remainingDays)  + " of " + month.name()); // We print the month's name and the leftover days
            } else { // Else, we decrease our tracked amount by the days in the current month and proceed to the next month
                remainingDays -= month.getNumDays();
            }
        }
        throw new IllegalArgumentException("Unknown invalid input. Please try again.");
    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.println("[DAY OF THE YEAR FROM DATE]");

        // GET MONTH INPUT

        boolean validMonth = false;
        String userInput = "";
        System.out.println("Please enter the name of a month:");
        do {
            String inputMonth = reader.nextLine();

            try {
                validMonth = validateInputMonth(inputMonth);
                userInput = inputMonth;

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        } while (!validMonth);

        // GET DAY INPUT
        MonthOfYear chosenMonth = MonthOfYear.valueOf(userInput.toUpperCase());
        int chosenDay = 0;

        boolean validDay = false;
        System.out.println("Please enter a day of the chosen month:");
        do {
            int inputDay = reader.nextInt();

            if (inputDay > 0 && inputDay <= chosenMonth.getNumDays()) {
                validDay = true;
                chosenDay = inputDay;
            } else {
                System.out.println("For the desired month, available days range from 1 to " + chosenMonth.getNumDays() + ".\nPlease check your input and try again:");
            }
        } while (!validDay);

        System.out.format("The %s of %s is the %s day of the year.", Utilities.ordinalize(chosenDay), chosenMonth, Utilities.ordinalize(dayOfYear(chosenDay, chosenMonth)));

        // GET DAY OF THE YEAR INPUT

        System.out.println("\n\n[DAY OF THE YEAR FROM DATE]");

        validDay = false;
        chosenDay = 0;
        String dateString = "";
        System.out.println("Please enter a day of the year:");
        do {
            int inputDay = reader.nextInt();

            try {
                dateString = dateFromDayOfYear(inputDay);
                chosenDay = inputDay;
                validDay = true;

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (!validDay);

        System.out.format("The %s day of the year is the %s", Utilities.ordinalize(chosenDay), dateString);
    }
}
