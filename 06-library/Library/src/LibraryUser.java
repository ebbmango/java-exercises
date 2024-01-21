import java.util.ArrayList;

public abstract sealed class LibraryUser permits Student, FacultyMember {
    // UNIQUE IDENTIFIER
    protected int id;
    private static int nextId = 1;

    // LOAN SETTINGS
    protected int[] maxLoanLength; // books, journals, movies
    protected int[] maxLoanAmount; // books, journals, movies

    // PROBABILITIES SETTINGS
    protected boolean isPunctual;
    private static final double[] LOAN_PROBABILITIES = {0.05, 0.08, 0.05}; // books, journals, movies
    private static final double RETURN_PROBABILITY = 0.02; // all items

    // USER'S INVENTORY
    private final ArrayList<LibraryItem> activeLoans = new ArrayList<>();
    private final int[] activeLoansCount = new int[3]; // books, journals, movies

    LibraryUser(boolean isPunctual) {
        this.id = nextId++;
        this.isPunctual = isPunctual;
    }

    // GETTERS: ACTIVE LOANS

    public int getActiveBookLoans() {
        return activeLoansCount[0];
    }
    public int getActiveJournalLoans() {
        return activeLoansCount[1];
    }
    public int getActiveMovieLoans() {
        return activeLoansCount[2];
    }

    // GETTERS: MAX LOAN AMOUNT

    public int getMaxBookLoanAmount() {
        return maxLoanAmount[0];
    }

    public int getMaxJournalLoanAmount() {
        return maxLoanAmount[1];
    }

    public int getMaxMovieLoanAmount() {
        return maxLoanAmount[2];
    }

    // GETTERS: MAX LOAN LENGTH

    public int getMaxBookLoanLength() {
        return maxLoanLength[0];
    }

    public int getMaxJournalLoanLength() {
        return maxLoanLength[1];
    }

    public int getMaxMovieLoanLength() {
        return maxLoanLength[2];
    }

    // GETTERS: PROBABILITIES

    public boolean willLoanBook() {
        return Utilities.roll(LOAN_PROBABILITIES[0]);
    }

    public boolean willLoanJournal() {
        return Utilities.roll(LOAN_PROBABILITIES[1]);
    }

    public boolean willLoanMovie() {
        return Utilities.roll(LOAN_PROBABILITIES[2]);
    }

//    public ArrayList<LibraryItem> willReturnItem () {
//        return Utilities.roll(
//    }

    public void loan(LibraryItem item) {
        String itemType = item.getClass().getName();

        switch (itemType) {
            case "Book":
                if (activeLoansCount[0] == maxLoanAmount[0]) {
                    throw new IllegalArgumentException("The user <" + id +"> cannot borrow any more books. Current amount: " + activeLoansCount[0]);
                } else {
                    activeLoansCount[0]++;
                    activeLoans.add(item);
                    item.borrow();
                }
                break;
            case "Journal":
                if (activeLoansCount[1] == maxLoanAmount[1]) {
                    throw new IllegalArgumentException("The user <" + id +"> cannot borrow any more journals. Current amount: " + activeLoansCount[1]);
                } else {
                    activeLoansCount[1]++;
                    activeLoans.add(item);
                    item.borrow();
                }
                break;
            case "Movie":
                if (activeLoansCount[2] == maxLoanAmount[2]) {
                    throw new IllegalArgumentException("The user <" + id +"> cannot borrow any more movies. Current amount: " + activeLoansCount[2]);
                } else {
                    activeLoansCount[2]++;
                    activeLoans.add(item);
                    item.borrow();
                }
                break;
        }

    }

}
