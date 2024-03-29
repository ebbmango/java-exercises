import java.util.ArrayList;

public abstract sealed class LibraryUser permits Student, FacultyMember {
    private static final double[] LOAN_PROBABILITIES = {0.05, 0.08, 0.05}; // books, journals, movies
    private static final double RETURN_PROBABILITY = 0.02; // all items
    private static int nextId = 1;
    private final ArrayList<LibraryItem> activeLoans = new ArrayList<>();
    private final int[] activeLoansCount = new int[3]; // books, journals, movies
    protected int id;
    protected int[] maxLoanLength; // books, journals, movies
    protected int[] maxLoanAmount; // books, journals, movies
    protected boolean isPunctual;
    private double balance;

    LibraryUser(boolean isPunctual) {
        this.id = nextId++;
        this.isPunctual = isPunctual;
        this.balance = 0;
    }

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

    // METHODS

    public ArrayList<LibraryItem> solveItemsReturn() {
        ArrayList<LibraryItem> itemsToReturn = new ArrayList<>();
        for (LibraryItem item : activeLoans) {
            if (Utilities.roll(RETURN_PROBABILITY) || (item.daysOverdue() == 0) && isPunctual) {
                itemsToReturn.add(item);
            }
        }

        for (LibraryItem item : itemsToReturn) {
            balance -= item.computeFine();
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

    public void show() {
        System.out.format("ID: %s <%s> <%s>\nActive Loans:\n", id, this.getClass().getName(), isPunctual ? "punctual" : "not punctual");
        for (LibraryItem activeLoan : activeLoans) {
            activeLoan.show();
        }
        System.out.format("Current Balance: %s\n", balance);
    }

}
