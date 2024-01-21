import java.util.Collection;

public abstract sealed class LibraryItem permits Book, Journal, Movie {
    protected int id;
    private static int nextId = 1;

    LibraryItem() {
        this.id = nextId++;
    }

    private int overdueDays;
    private boolean isAvailable = true;

//    private

    protected double dailyFee;

    public void show() {
        // meant to be overridden
    }

//    public boolean isOverdue() {
//
//    }
//
    public int daysOverdue() {
        return overdueDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void borrow(LibraryUser user) {
        switch (this.getClass().getName()) {
            case "Book":
                overdueDays = -user.getMaxBookLoanLength();
                break;
            case "Journal":
                overdueDays = -user.getMaxJournalLoanLength();
                break;
            case "Movie":
                overdueDays = -user.getMaxMovieLoanLength();
                break;
        }
        isAvailable = false;
    }

    public void returnToLibrary() {
        isAvailable = true;
    }

    public double getDailyFee() {
        return dailyFee;
    }
}
