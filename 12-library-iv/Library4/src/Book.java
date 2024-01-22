
public final class Book extends LibraryItem {
//    ADDITION:
    private final int pagesNr = 100;
    private final String title;
    private final String author;
    private final String genre;
    private final String publisher;

    public Book(String title, String author, String genre, String publisher) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.dailyFee = 0.5;
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.genre = "Unknown";
        this.publisher = "Unknown";
        this.dailyFee = 0.5;
    }

    private Book(String title) {
        this.title = title;
        this.author = "Unknown";
        this.genre = "Unknown";
        this.publisher = "Unknown";
        this.dailyFee = 0.5;
    }

    public void show() {
        System.out.format("Title: %s / Author: %s / Genre: %s / Publisher: %s\n", Utilities.printTag(title), Utilities.printTag(author), Utilities.printTag(genre), Utilities.printTag(publisher));
        super.show();
    }
}
