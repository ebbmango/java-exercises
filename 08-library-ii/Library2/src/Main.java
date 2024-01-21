public class Main {
    public static void main(String[] args) {
        Library library = new Library(//
                "./src/movies.csv",   // path to movies' CSV file
                "./src/journals.csv", // path to journals' CSV file
                "./src/books.csv",    // path to books' CSV file
                20,                   // desired amount of faculty members
                80,                   // desired amount of students
                67,                   // desired amount of punctual users
                -500);                // debt threshold for blocking a user's account

        // Simulating the operation of the library over a year
        for (int i = 0; i < 365; i++) {
            // TASK:
            // "Call the corresponding methods for at least two users and two different days."

            // At some point at least two users are bound to get blocked due to their unpaid balances.
            library.passDay();

            // And here we are displaying the debtors for two different days:
            if (i == 150 || i == 300) {
                library.displayDebtors();
            }

        }
    }
}

