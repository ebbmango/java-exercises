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
        overdueDays++;
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


    public void show() {
        if (isAvailable) {
            System.out.println("Currently available.");
        } else {
            System.out.format("Days overdue: %s / Daily fee: %s / Total fine: %s\n", overdueDays, dailyFee, computeFine());
        }
    }
}
