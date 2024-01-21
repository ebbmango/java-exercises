import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Library {
    // INSTANCE VARIABLES
    ArrayList<LibraryItem> inventory = new ArrayList<>();
    ArrayList<LibraryUser> users = new ArrayList<>();

    // CONSTRUCTOR
    public Library(String moviesPath, String journalsPath, String booksPath) {
        loadItems( // load all movies from their CSV file into the inventory
                moviesPath,values -> inventory.add(new Movie(values[1], // title
                        values[2], // genre
                        values[4], // director
                        values[6], // year
                        values[7], // runtime
                        values[8]  // rating
                )));
        loadItems( // load all journals from their CSV file into the inventory
                journalsPath, values -> inventory.add(new Journal(values[0],  // title
                        values[3],  // eISSN
                        values[4],  // publisher,
                        values[6],  // latest issue
                        values[12]  // URL
                )));
        loadItems( // load all books from their CSV file into the inventory
                booksPath, values -> inventory.add(new Book(values[0], // title
                        values[1], // author
                        values[2], // genre
                        values[4]  // publisher
                )));
    }

    private void loadItems(String path, Consumer<String[]> consumer) {
        // setting up the configuration for the parsing of the CSV file by the external library
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setDelimiter(";").setSkipHeaderRecord(true).build();

        // reading the CSV file
        try (Reader inputStream = new FileReader(path)) {
            // parsing the CSV file
            CSVParser records = csvFormat.parse(inputStream);
            // funnelling each record (as an array of strings) into the consumer passed into the loader function
            for (CSVRecord record : records) {
                consumer.accept(record.values());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printItems() {
        for (LibraryItem item : inventory) {
            item.show();
        }
    }

}

