public abstract sealed class LibraryItem permits Book, Journal, Movie {
    private static int nextId = 1;
    protected int id;
    protected double dailyFee;
    private int overdueDays;
    private boolean isAvailable = true;

    LibraryItem() {
        this.id = nextId++;
    }

    public void passDay() {
        overdueDays += 1;
    }

    public int daysOverdue() {
        return overdueDays;
    }

    public boolean isOverdue() {
        return overdueDays > 0;
    }

    public double computeFine() {
        return isOverdue() ? dailyFee * overdueDays : 0;
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

//    ADDITION
    @Override
    public String toString() {
        if (isAvailable) {
            return "Currently available.";
        } else {
            return String.format("Days overdue: %s / Daily fee: %s / Total fine: %s\n", overdueDays, dailyFee, computeFine());
        }
    }
}
