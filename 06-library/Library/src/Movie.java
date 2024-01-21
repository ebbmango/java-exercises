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

    public void show() {
        System.out.format("%s / %s / %s / %s / %s / %s \n", title, genre, director, year, runtime, rating);
    }

}
