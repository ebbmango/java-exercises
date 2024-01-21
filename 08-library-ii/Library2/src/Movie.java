public final class Movie extends LibraryItem {
    private final String title;
    private final String genre;
    private final String director;

    // made into Strings for inability to fix "NumberFormatException"
    private final String year;
    private final String runtime;
    private final String rating;

    public Movie(String title, String genre, String director, String year, String runtime, String rating) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.year = year;
        this.runtime = runtime;
        this.rating = rating;
        this.dailyFee = 5.0;
    }

//    ADDITION
    @Override
    public String toString() {
        return String.format("Title: %s / Genre: %s / Director: %s / Year: %s / Runtime: %s / Rating: %s \n", Utilities.printTag(title), Utilities.printTag(genre), Utilities.printTag(director), Utilities.printTag(year), Utilities.printTag(runtime), Utilities.printTag(rating)) + super.toString();
    }

}
