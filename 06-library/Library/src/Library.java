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
    public Library(String moviesPath, String journalsPath, String booksPath, int facultyMembersAmount, int studentsAmount, int punctualUsersAmount) {
        loadUsers(facultyMembersAmount, studentsAmount, punctualUsersAmount);
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

    private void loadUsers(int facultyMembers, int studentsAmount, int punctualAmount) {
        int usersAmount = facultyMembers + studentsAmount;

        if (punctualAmount > usersAmount) {
            throw new IllegalArgumentException("Invalid input: Punctual people cannot be greater than total people.");
        }

        // we create the array of arrays of booleans
        boolean[][] members = new boolean[usersAmount][2];

        // we fill it with the desired amount of facultyMembers
        for (int i = 0; i < facultyMembers; i++) {
            members[i] = new boolean[]{true, false};
        }

        // we shuffle the array
        Utilities.shuffleArray(members);

        // we make 66 random members punctual
        for (int i = 0; i < punctualAmount; i++) {
            members[i][1] = true;
        }

        // we load each member into the usersArrayList
        for (boolean[] member : members) {
            boolean isFacultyMember = member[0];
            boolean isPunctual = member[1];
            users.add(generateUser(isFacultyMember, isPunctual));
        }
    }

    private LibraryUser generateUser(boolean isFacultyMember, boolean isPunctual) {
        if (isFacultyMember) {
            return new FacultyMember(isPunctual);
        } else {
            return new Student(isPunctual);
        }
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

//    public void printUsers() {
//        int facultyMembers = 0;
//        int students = 0;
//
//        for (LibraryUser user : users) {
//            System.out.println(user);
//            if (user.getClass().getName().equals("FacultyMember")) {
//                facultyMembers++;
//            }
//            if (user.getClass().getName().equals("Student")) {
//                students++;
//            }
//        }
//
//        System.out.println(facultyMembers + " " + students);
//    }

//    public void printItems() {
//        for (LibraryItem item : inventory) {
//            item.show();
//        }
//    }

}

