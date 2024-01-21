public final class Book extends LibraryItem {
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

    public void show() {
        System.out.format("%s / %s / %s / %s\n", title, author, genre, publisher);
        super.show();
    }
}
