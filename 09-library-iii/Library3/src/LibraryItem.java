public abstract sealed class LibraryItem permits Book, Journal, Movie {
    private static int nextId = 1;
    protected int id;
    protected double dailyFee;
    protected int overdueDays;
    protected boolean isAvailable = true;

    protected LibraryUser loanedTo = null; // ADDITION

    public LibraryUser getBorrower() {
        return loanedTo;
    }

    public int getID() {
        return id;
    }

    public LibraryItem() {
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
        loanedTo = user; // ADDITION
    }

    public void returnToLibrary() {
        loanedTo = null; // ADDITION
        isAvailable = true;
        overdueDays = 0;
    }

    public double getDailyFee() {
        return dailyFee;
    }

    @Override
    public String toString() {
        if (isAvailable) {
            return "Currently available.";
        } else {
            return String.format("Days overdue: %s / Daily fee: %s / Total fine: %s\n", overdueDays, dailyFee, computeFine());
        }
    }
}
