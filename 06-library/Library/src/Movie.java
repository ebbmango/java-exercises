public class Movie extends LibraryItem {
    private String title;
    private String genre;
    private String director;
    private int year;
    private int runtime;
    private double rating;

    public Movie (String title, String genre , String director , int year , int runtime , double rating) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.year = year;
        this.runtime = runtime;
        this.rating = rating;
    }

}
