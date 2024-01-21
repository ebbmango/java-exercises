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

    public void passDay() {
        overdueDays++;
    }

    public void show() {
        // meant to be overridden
    }

    public int daysOverdue() {
        return overdueDays;
    }

    public boolean isOverdue() {
        return overdueDays > 0;
    }

    public double computeFine() {
        return dailyFee * overdueDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void borrowItem(LibraryUser user) {
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
        overdueDays = 0;
    }

    public double getDailyFee() {
        return dailyFee;
    }
}
