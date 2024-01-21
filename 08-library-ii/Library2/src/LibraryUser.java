import java.util.ArrayList;

public abstract sealed class LibraryUser implements LibraryUserInterface permits Student, FacultyMember {
    public int getID() {
        return id;
    }
    private static final double[] LOAN_PROBABILITIES = {0.05, 0.08, 0.05}; // books, journals, movies
    private static final double RETURN_PROBABILITY = 0.02; // all items
    private static int nextId = 1;
    private final ArrayList<LibraryItem> activeLoans = new ArrayList<>();
    private final int[] activeLoansCount = new int[3]; // books, journals, movies
    protected int id;
    protected int[] maxLoanAmount; // books, journals, movies
    protected int[] maxLoanLength; // books, journals, movies
    protected boolean isPunctual;
    private double balance;

    LibraryUser(boolean isPunctual) {
        this.id = nextId++;
        this.isPunctual = isPunctual;
        this.balance = 0;
    }

    public ArrayList<LibraryItem> getActiveLoans() { return activeLoans; }

    public int getActiveBookLoans() {
        return activeLoansCount[0];
    }

    public int getActiveJournalLoans() {
        return activeLoansCount[1];
    }

    public int getActiveMovieLoans() {
        return activeLoansCount[2];
    }

    public int getMaxBookLoanAmount() {
        return maxLoanAmount[0];
    }

    public int getMaxJournalLoanAmount() {
        return maxLoanAmount[1];
    }

    public int getMaxMovieLoanAmount() {
        return maxLoanAmount[2];
    }

    public int getMaxBookLoanLength() {
        return maxLoanLength[0];
    }

    public int getMaxJournalLoanLength() {
        return maxLoanLength[1];
    }

    public int getMaxMovieLoanLength() {
        return maxLoanLength[2];
    }

    public boolean willLoanBook() {
        return Utilities.roll(LOAN_PROBABILITIES[0]);
    }

    public boolean willLoanJournal() {
        return Utilities.roll(LOAN_PROBABILITIES[1]);
    }

    public boolean willLoanMovie() {
        return Utilities.roll(LOAN_PROBABILITIES[2]);
    }

    // ADDITION
    public void updateBalance() {
        balance -= getDailyBalance();
    }

    // ADDITION
    public double getBalance() {
        return balance;
    }

    public ArrayList<LibraryItem> solveItemsReturn() {
        ArrayList<LibraryItem> itemsToReturn = new ArrayList<>();
        for (LibraryItem item : activeLoans) {
            if (Utilities.roll(RETURN_PROBABILITY) || (item.daysOverdue() == 0) && isPunctual) {
                itemsToReturn.add(item);
            }
        }

        for (LibraryItem item : itemsToReturn) {
// It seems I misinterpreted last assignment's instructions, it said:
// "Design the method computeFine, that computes the fine for this item, if the item is returned with the delay."
// And I interpreted that the fine should only be computed once the item was returned.
// The fine should, however, be updated DAILY.
// That is why the next line of code is commented out: it is part of the old implementation that has been replaced.

//          MODIFICATION:
//          balance -= item.computeFine(); // <COMMENTED OUT>
            activeLoans.remove(item);
            item.returnToLibrary();
        }

        return itemsToReturn;
    }

    public void loan(LibraryItem item) {
        String itemType = item.getClass().getName();

        switch (itemType) {
            case "Book":
                if (activeLoansCount[0] == maxLoanAmount[0]) {
                    throw new IllegalArgumentException("The user <" + id + "> cannot borrow any more books. Current amount: " + activeLoansCount[0]);
                } else {
                    activeLoansCount[0]++;
                    activeLoans.add(item);
                    item.borrowItem(this);
                }
                break;
            case "Journal":
                if (activeLoansCount[1] == maxLoanAmount[1]) {
                    throw new IllegalArgumentException("The user <" + id + "> cannot borrow any more journals. Current amount: " + activeLoansCount[1]);
                } else {
                    activeLoansCount[1]++;
                    activeLoans.add(item);
                    item.borrowItem(this);
                }
                break;
            case "Movie":
                if (activeLoansCount[2] == maxLoanAmount[2]) {
                    throw new IllegalArgumentException("The user <" + id + "> cannot borrow any more movies. Current amount: " + activeLoansCount[2]);
                } else {
                    activeLoansCount[2]++;
                    activeLoans.add(item);
                    item.borrowItem(this);
                }
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder userInfo = new StringBuilder(String.format("ID: %s <%s> <%s>\nActive Loans:\n", id, this.getClass().getName(), isPunctual ? "punctual" : "not punctual"));
        for (LibraryItem activeLoan : activeLoans) {
            userInfo.append(activeLoan.toString()).append("\n");
        }
        userInfo.append("Current Balance: %s").append(balance);
        return userInfo.toString();
    }

}
