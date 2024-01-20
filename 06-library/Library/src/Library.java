import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.util.function.Consumer;
import java.util.ArrayList;

public class Library {
    // INSTANCE VARIABLES
    ArrayList<LibraryItem> inventory = new ArrayList<>();

    // CONSTRUCTOR
    public Library (String csvFilePath) {
        loadItems(
                csvFilePath,
                values -> inventory.add(new Book(values[0], values[1], values[2], values[4]))
        );
    }

    private void loadItems(String path, Consumer<String[]> consumer) {
        // setting up the configuration for the parsing of the CSV file by the external library
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setDelimiter(";").setSkipHeaderRecord(true).build();

        // reading the CSV file
        try (Reader inputStream = new FileReader(path)) { // c
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

