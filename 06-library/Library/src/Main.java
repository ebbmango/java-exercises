public class Main {
    public static void main(String[] args) {
        Library library = new Library("./src/movies.csv", "./src/journals.csv", "./src/books.csv", 20, 80, 67);

        // Simulating the operation of the library over a year
        for (int i = 0; i < 365; i++) {
            library.passDay();
        }
    }
}

