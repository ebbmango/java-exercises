public class Main {
    public static void main(String[] args) {
        Library library = new Library("./src/movies.csv", "./src/journals.csv", "./src/books.csv", 20, 80, 67);

//        for (int i = 0; i < 365; i++) {
//            library.passDay();
//        }

        var users = library.getUsers();
        var items = library.getInventory();

        var user1 = users.getFirst();
        var item1 = items.get(1);

        user1.show();
        item1.show();

        library.passDay();

        item1.borrowItem(user1);
        System.out.println();

        user1.show();
        item1.show();

//        library.showItems();

    }
}

