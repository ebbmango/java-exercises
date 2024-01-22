import java.util.ArrayList;
import java.util.List;



public class Main {
    public static void main(String[] args) {
        Library library = new Library(//
                "./src/movies.csv",   // path to movies' CSV file
                "./src/journals.csv", // path to journals' CSV file
                "./src/books.csv",    // path to books' CSV file
                20,                   // desired amount of faculty members
                80,                   // desired amount of students
                67,                   // desired amount of punctual users
                -500);                // debt threshold for blocking a user's account


//      LOCAL INNER CLASS - Custom data holder that holds an item, its availability and this item's last borrower
        class pair {
            public LibraryItem item;
            public LibraryUser lastBorrower;
            public boolean available;

            public pair (LibraryItem item, boolean available) {
                this.item = item;
                this.available = available;
                this.lastBorrower = item.getBorrower();
            }
        }
        // interface used to instantiate anonymous inner class
        interface Logger {
            void returnDayLog(int day, List<pair> pairsNow, List<pair> pairsThen);

            void getLog(int day);
        }

        // ONE-TIME-OBJECT CREATED FROM INSTANTIATION OF ANONYMOUS INNER CLASS implementing the "Logger" interface
        Logger logger = new Logger () {
            final private String[] logs = new String[365];
            public void getLog(int day) {
                System.out.println(logs[day - 1]);
            }
            public void returnDayLog (int day, List<pair> pairsThen, List<pair> pairsNow) {

                StringBuilder dailyReport = new StringBuilder("[DAY " + (day + 1) + " REPORT]:");

                for (int i = 0; i < pairsThen.size(); i++) {

                    var pairThen = pairsThen.get(i);
                    var pairNow = pairsNow.get(i);

                    if (pairThen.available && !pairNow.available) {
                        dailyReport.append(String.format("\nThe item <ID:%s> has been borrowed by the user <ID:%s>.", pairNow.item.getID(), pairNow.item.getBorrower().getID()));
                    }
                    if (!pairThen.available && pairNow.available) {
                        dailyReport.append(String.format("\nThe item <ID:%s> has been RETURNED by the user <ID:%s>.", pairNow.item.getID(), pairThen.lastBorrower.getID()));
                    }
                }
                logs[day] = dailyReport.toString();
            }
        };

        // CODE RESUMES HERE

        for (int i = 0; i < 365; i++) {
            ArrayList<pair> oldPairs = new ArrayList<>();
            for (var item : library.getItems()) {
                oldPairs.add(new pair(item, item.isAvailable()));
            }

            library.passDay();

            ArrayList<pair> newPairs = new ArrayList<>();
            for (var item : library.getItems()) {
                newPairs.add(new pair(item, item.isAvailable()));
            }

            logger.returnDayLog(i, oldPairs, newPairs);
        }

//        you can log the loans and returns of whatever days you would like here :)
        logger.getLog(1);
        logger.getLog(2);
        logger.getLog(3);


    }
}

