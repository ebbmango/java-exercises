public final class Journal extends LibraryItem {
    private final String title;
    private final String eISSN;
    private final String publisher;
    private final String latestIssue;
    private final String URL;

    public Journal(String title, String eISSN, String publisher, String latestIssue, String URL) {
        this.title = title;
        this.eISSN = eISSN;
        this.publisher = publisher;
        this.latestIssue = latestIssue;
        this.URL = URL;
        this.dailyFee = 2.0;
    }

//    ADDITION
    @Override
    public String toString() {
        return String.format("Title: %s / e-ISSN: %s / Publisher: %s / Latest Issue: %s / URL: %s\n", Utilities.printTag(title), Utilities.printTag(eISSN), Utilities.printTag(publisher), Utilities.printTag(latestIssue), Utilities.printTag(URL)) + super.toString();
    }

}
