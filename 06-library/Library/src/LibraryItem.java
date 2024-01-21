public abstract sealed class LibraryItem permits Book, Journal, Movie {

    protected double dailyFee;

    public void show() {
        // meant to be overridden
    }

    public double getDailyFee() {
        return dailyFee;
    }
}
