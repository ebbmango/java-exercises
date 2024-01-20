public final class Movie extends LibraryItem {
    private final String title;
    private final String genre;
    private final String director;
    private final int year;
    private final int runtime;
    private final double rating;

    public Movie (String title, String genre , String director , int year , int runtime , double rating) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.year = year;
        this.runtime = runtime;
        this.rating = rating;
    }

}
