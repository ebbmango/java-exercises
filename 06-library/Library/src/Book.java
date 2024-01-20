public class Book extends LibraryItem {
    private String title;
    private String author;
    private String genre;
    private String publisher;

    public Book(String title, String author, String genre, String publisher) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
    }
}
