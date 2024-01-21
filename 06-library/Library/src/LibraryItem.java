import java.util.Collection;

public abstract sealed class LibraryItem permits Book, Journal, Movie {
    protected int id;
    private static int nextId = 1;

    LibraryItem() {
        this.id = nextId++;
    }

    private int daysOverdue;
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
//    public int daysOverdue() {
//
//    }

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
