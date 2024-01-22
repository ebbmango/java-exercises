// 1) "Create a Java enum class named MonthOfYear"
public enum MonthOfYear {
    // "The constants for the enum will be the months of the year: JANUARY through DECEMBER."
    JANUARY(1, 31),
    FEBRUARY(2, 28),
    MARCH(3, 31),
    APRIL(4, 30),
    MAY(5, 31),
    JUNE(6, 30),
    JULY(7, 31),
    AUGUST(8, 31),
    SEPTEMBER(9, 30),
    OCTOBER(10, 31),
    NOVEMBER(11, 30),
    DECEMBER(12, 31);

    private final int numOfMonth;

    // 2) "Create a field for the enum which shold the days of the months."
    // "Thus the field should be declared: private final int numDays;"
    private final int numDays;

    // 3) "Create a constructor in the enum to initialize the number of days in each month controlled by the enum."
    MonthOfYear(int numOfMonth, int numDays) {
        // "The constructor should initialize numDays with the number of days for each month of the year, assuming we have no leap year"
        this.numOfMonth = numOfMonth;
        this.numDays = numDays;
    }

    //to get the number of days in a month:
    public int getNumDays() {
        return numDays;
    }
    // 4) "Add an accessor (getter) method named getNumDays() which will return an integer specifying the number of days in the month."
    public int getMonthPosition() {
        return numOfMonth;
    }
}
