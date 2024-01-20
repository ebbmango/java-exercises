public class Journal extends LibraryItem {
    private final String title;
    private final String eISSN;
    private final String publisher;
    private final String latestIssue;
    private final String URL;

    public Journal (String title, String eISSN , String publisher , String latestIssue , String URL) {
        this.title = title;
        this.eISSN = eISSN;
        this.publisher = publisher;
        this.latestIssue = latestIssue;
        this.URL = URL;
    }
}
