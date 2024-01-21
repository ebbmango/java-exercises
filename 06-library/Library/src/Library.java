import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
// external libraries

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
// imports for file reading

import java.util.ArrayList;
import java.util.function.Consumer;
// imports for data handling

public class Library {
    // INSTANCE VARIABLES
    ArrayList<LibraryItem> inventory = new ArrayList<>();
    ArrayList<LibraryUser> users = new ArrayList<>();

    // CONSTRUCTOR
    public Library(String moviesPath, String journalsPath, String booksPath, int facultyMembersAmount, int studentsAmount, int punctualUsersAmount) {
        loadUsers(facultyMembersAmount, studentsAmount, punctualUsersAmount);
        loadItems( // load all movies from their CSV file into the inventory
                moviesPath, values -> inventory.add(new Movie(values[1], // title
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

    // OBS: definitely not the most efficient solution
    private ArrayList<LibraryItem> getAvailableItems(String itemType) {
        ArrayList<LibraryItem> availableItems = new ArrayList<>();

        switch (itemType) {
            case "Book":
                for (LibraryItem item : inventory) {
                    if (item instanceof Book && item.isAvailable()) availableItems.add(item);
                }
                break;
            case "Journal":
                for (LibraryItem item : inventory) {
                    if (item instanceof Journal && item.isAvailable()) availableItems.add(item);
                }
                break;
            case "Movie":
                for (LibraryItem item : inventory) {
                    if (item instanceof Movie && item.isAvailable()) availableItems.add(item);
                }
                break;
            default:
                throw new IllegalArgumentException("There are no items of the type \"" + itemType + "\".");
        }

        return availableItems;
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

    private void borrowRandom(ArrayList<LibraryItem> availableItems, LibraryUser user) {
        int pickedItemIndex = Utilities.getRandomIndex(availableItems);

        // if there are still available items...
        if (pickedItemIndex != -1) {
            LibraryItem pickedItem = availableItems.get(pickedItemIndex); // we get the item
            availableItems.remove(pickedItemIndex); // we remove the item from the list of availabilities

            user.loan(pickedItem); // we loan it under the user's name
        }
    }

    private void refillAvailabilities(LibraryItem returnedItem, ArrayList<LibraryItem> availableBooks, ArrayList<LibraryItem> availableJournals, ArrayList<LibraryItem> availableMovies) {
        switch (returnedItem.getClass().getName()) {
            case "Book":
                availableBooks.add(returnedItem);
                break;
            case "Journal":
                availableJournals.add(returnedItem);
                break;
            case "Movie":
                availableMovies.add(returnedItem);
                break;

        }
    }

    // OBS: holy cow this code will explode anyone's computer
    public void passDay() {
        // code here what happens whenever a day passes

//        HERE WE ARE MAKING AVAILABILITIES LISTS FOR EVERY DAY AND UPDATING IT ACCORDING TO EACH USER'S ACTIONS
        ArrayList<LibraryItem> availableBooks = getAvailableItems("Book");
        ArrayList<LibraryItem> availableJournals = getAvailableItems("Journal");
        ArrayList<LibraryItem> availableMovies = getAvailableItems("Movie");

        for (LibraryItem item : inventory) {
            item.passDay();
        }

        for (LibraryUser user : users) {
            // HOW EACH USER BEHAVES FOR EACH DAY
            // IF IT IS A STUDENT
            // -> Borrowing
            if (user instanceof Student) {
                // if the user can still loan BOOKS
                if (user.getActiveBookLoans() < user.getMaxBookLoanAmount()) {
                    // roll the dice to see if the user will borrow a book
                    if (user.willLoanBook()) { // if they do...
                        borrowRandom(availableBooks, user); // we borrow a random book under their name
                    }
                }
                // if the user can still loan JOURNALS
                if (user.getActiveJournalLoans() < user.getMaxJournalLoanAmount()) {
                    // roll the dice to see if the user will borrow a journal
                    if (user.willLoanJournal()) { // if they do...
                        borrowRandom(availableJournals, user); // we borrow a random journal under their name
                    }
                }
                // if the user can still loan MOVIES
                if (user.getActiveMovieLoans() < user.getMaxMovieLoanAmount()) {
                    // roll the dice to see if the user will borrow a movie
                    if (user.willLoanMovie()) { // if they do...
                        borrowRandom(availableMovies, user); // we borrow a random movie under their name
                    }
                }
                // -> Returning
                // get the list of items that the user will return (if any)
                for (LibraryItem returnedItem : user.solveItemsReturn()) {
                    // and add them back to the availabilities' lists
                    refillAvailabilities(returnedItem, availableBooks, availableJournals, availableMovies);
                }

            } // end if Student

            // IF IT IS A FACULTY MEMBER
            if (user instanceof FacultyMember) {

                // -> Borrowing
                if (user.willLoanBook()) {
                    borrowRandom(availableBooks, user);
                }
                if (user.willLoanJournal()) {
                    borrowRandom(availableJournals, user);
                }
                if (user.willLoanMovie()) {
                    borrowRandom(availableMovies, user);
                }

                // -> Returning
                for (LibraryItem returnedItem : user.solveItemsReturn()) {
                    refillAvailabilities(returnedItem, availableBooks, availableJournals, availableMovies);
                }

            } // end if Faculty Member
        }
    }
}

