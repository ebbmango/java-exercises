public abstract sealed class LibraryItem permits Book, Journal, Movie {
    private boolean isAvailable = true;
    protected double dailyFee;

    public void show() {
        // meant to be overridden
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void borrow() {
        isAvailable = false;
    }

    public double getDailyFee() {
        return dailyFee;
    }
}
