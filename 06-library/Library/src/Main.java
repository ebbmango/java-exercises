// imports for dealing with CSV
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
// imports for dealing with reading from files
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class Main {
    public static void main(String[] args) {
        Library library = new Library("./src/books.csv");
        library.printItems();
    }
}

