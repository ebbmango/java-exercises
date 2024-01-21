import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
// external libraries

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class Library {
    ArrayList<LibraryItem> inventory = new ArrayList<>();
    ArrayList<LibraryUser> users = new ArrayList<>();

    public Library(String moviesPath, String journalsPath, String booksPath, int facultyMembersAmount, int studentsAmount, int punctualUsersAmount) {
        loadUsers(facultyMembersAmount, studentsAmount, punctualUsersAmount);
        loadItems(
                moviesPath, values -> inventory.add(new Movie(
                        values[1], // title
                        values[2], // genre
                        values[4], // director
                        values[6], // year
                        values[7], // runtime
                        values[8]  // rating
                )));
        loadItems(
                journalsPath, values -> inventory.add(
                        new Journal(values[0],  // title
                        values[3],  // eISSN
                        values[4],  // publisher,
                        values[6],  // latest issue
                        values[12]  // URL
                )));
        loadItems(
                booksPath, values -> inventory.add(
                        new Book(values[0], // title
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

        boolean[][] members = new boolean[usersAmount][2];

        for (int i = 0; i < facultyMembers; i++) {
            members[i] = new boolean[]{true, false};
        }

        Utilities.shuffleArray(members);

        for (int i = 0; i < punctualAmount; i++) {
            members[i][1] = true;
        }

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
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setDelimiter(";").setSkipHeaderRecord(true).build();

        try (Reader inputStream = new FileReader(path)) {
            CSVParser records = csvFormat.parse(inputStream);
            for (CSVRecord record : records) {
                consumer.accept(record.values());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void borrowRandom(ArrayList<LibraryItem> availableItems, LibraryUser user) {
        int pickedItemIndex = Utilities.getRandomIndex(availableItems);

        if (pickedItemIndex != -1) {
            LibraryItem pickedItem = availableItems.get(pickedItemIndex);
            availableItems.remove(pickedItemIndex);
            user.loan(pickedItem);
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

    public void passDay() {
        ArrayList<LibraryItem> availableBooks = getAvailableItems("Book");
        ArrayList<LibraryItem> availableJournals = getAvailableItems("Journal");
        ArrayList<LibraryItem> availableMovies = getAvailableItems("Movie");

        for (LibraryItem item : inventory) {
            item.passDay();
        }

        for (LibraryUser user : users) {
            // ADDITION:
            user.updateBalance(); // charges the user for each unreturned overdue item currently at their possession

            if (user instanceof Student) {
                if (user.getActiveBookLoans() < user.getMaxBookLoanAmount()) {
                    if (user.willLoanBook()) {
                        borrowRandom(availableBooks, user);
                    }
                }
                if (user.getActiveJournalLoans() < user.getMaxJournalLoanAmount()) {
                    if (user.willLoanJournal()) {
                        borrowRandom(availableJournals, user);
                    }
                }
                if (user.getActiveMovieLoans() < user.getMaxMovieLoanAmount()) {
                    if (user.willLoanMovie()) {
                        borrowRandom(availableMovies, user);
                    }
                }
                for (LibraryItem returnedItem : user.solveItemsReturn()) {
                    refillAvailabilities(returnedItem, availableBooks, availableJournals, availableMovies);
                }
            }
            if (user instanceof FacultyMember) {
                if (user.willLoanBook()) {
                    borrowRandom(availableBooks, user);
                }
                if (user.willLoanJournal()) {
                    borrowRandom(availableJournals, user);
                }
                if (user.willLoanMovie()) {
                    borrowRandom(availableMovies, user);
                }
                for (LibraryItem returnedItem : user.solveItemsReturn()) {
                    refillAvailabilities(returnedItem, availableBooks, availableJournals, availableMovies);
                }
            }
        }
    }

//    ADDITION
    public List<LibraryUser> getDebtors () {
        return users.stream().filter(user -> user.getActiveLoans().stream().anyMatch(LibraryItem::isOverdue)).toList();
    }
}

