public class Main {
    public static void main(String[] args) {
        Library library = new Library("./src/movies.csv", "./src/journals.csv", "./src/books.csv", 20, 80, 67);

        for (int i = 0; i < 365; i++) {
            library.passDay();
        }

//        var items = library.getAvailableItems("Book");
//        for ( var item : items ) {
//            System.out.println(item);
//        }
//
//        System.out.println(Utilities.getRandomIndex(items));
    }
}

