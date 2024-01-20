public class Journal extends LibraryItem {
    private String title;
    private String eISSN;
    private String publisher;
    private String latestIssue;
    private String URL;

    public Journal (String title, String eISSN , String publisher , String latestIssue , String URL) {
        this.title = title;
        this.eISSN = eISSN;
        this.publisher = publisher;
        this.latestIssue = latestIssue;
        this.URL = URL;
    }
}
