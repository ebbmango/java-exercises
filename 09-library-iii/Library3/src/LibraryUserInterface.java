import java.util.ArrayList;

public interface LibraryUserInterface {
    /**
     * Returns the {@link LibraryUser}'s unique identifier
     */
    int getID();

    /**
     * Returns the amount of {@link Book}s the {@link LibraryUser} has loaned and currently has under their possession
     */
    int getActiveBookLoans();

    /**
     * Returns the amount of {@link Journal}s the {@link LibraryUser} has loaned and currently has under their possession
     */
    int getActiveJournalLoans();

    /**
     * Returns the amount of {@link Movie}s the {@link LibraryUser} has loaned and currently has under their possession
     */
    int getActiveMovieLoans();

    // GETTERS: MAX LOAN AMOUNT
    /**
     * Returns the maximum amount of {@link Book}s from the {@link Library} the {@link LibraryUser} can have under their possession at a time
     */
    int getMaxBookLoanAmount();

    /**
     * Returns the maximum amount of {@link Journal}s from the {@link Library} the {@link LibraryUser} can have under their possession at a time
     */
    int getMaxJournalLoanAmount();

    /**
     * Returns the maximum amount of {@link Movie}s from the {@link Library} the {@link LibraryUser} can have under their possession at a time
     */
    int getMaxMovieLoanAmount();

    // GETTERS: MAX LOAN LENGTH

    /**
     * Returns the maximum amount of days the {@link LibraryUser} can keep a {@link Book} from the {@link Library} under their possession
     */
    int getMaxBookLoanLength();

    /**
     * Returns the maximum amount of days the {@link LibraryUser} can keep a {@link Journal} from the {@link Library} under their possession
     */
    int getMaxJournalLoanLength();

    /**
     * Returns the maximum amount of days the {@link LibraryUser} can keep a {@link Movie} from the {@link Library} under their possession
     */
    int getMaxMovieLoanLength();

    // GETTERS: PROBABILITIES

    /**
     * Tests for the {@link LibraryUser}'s probability of borrowing a {@link Book} and returns whether they will
     */
    boolean willLoanBook();

    /**
     * Tests for the {@link LibraryUser}'s probability of borrowing a {@link Journal} and returns whether they will
     */
    boolean willLoanJournal();

    /**
     * Tests for the {@link LibraryUser}'s probability of borrowing a {@link Movie} and returns whether they will
     */
    boolean willLoanMovie();

    /**
     * For each {@link LibraryItem} currently under the {@link LibraryUser}'s possession, checks if they are going to return it to the {@link Library} and if so, performs such return
     */
    ArrayList<LibraryItem> solveItemsReturn();

    /**
     * Borrows a {@link LibraryItem} from the {@link Library} and adds it to the {@link LibraryUser}'s list of loans
     */
    void loan(LibraryItem item);

    /**
     * Returns an ArrayList of all the {@link LibraryUser}'s active loans
     */
    ArrayList<LibraryItem> getActiveLoans();

    /**
     * Returns the {@link LibraryUser}'s account balance FOR THE DAY
     */
    default double getDailyBalance() {
        double dailyBalance = 0;
        for (LibraryItem activeLoan : getActiveLoans()) {
            dailyBalance += activeLoan.isOverdue() ? activeLoan.getDailyFee() : 0;
        }
        return dailyBalance;
    }

    /**
     * Subtracts the current daily balance from the {@link LibraryUser}'s balance
     */
    void updateBalance();

    /**
     * Returns the {@link LibraryUser}'s account balance
     */
    double getBalance();

    /**
     * Displays the {@link LibraryUser}'s active loans ({@link LibraryItem})
     */
    default void displayActiveLoans() {
        System.out.format("The user <ID:%s> currently possesses the following loans:\n", getID());
        for (LibraryItem item : getActiveLoans()) {
            System.out.println(item.toString());
        }
    }

    /**
     * Returns the {@link String} representation of the {@link LibraryUser}
     */
    String toString();

}
