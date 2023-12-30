public class Main {

    public static void main(String[] args) {

        System.out.println("Initializing the Client objects:");
        System.out.println();

        Client client01 = new Client("Emanuel");
        Client client02 = new Client("Błażej");
        Client client03 = new Client();

        System.out.println();
        System.out.println("Adding the clients to the store...");
        System.out.println();

        Store store = new Store();
        store.add(client01);
        store.add(client02);
        store.add(client03);

        System.out.println("Invoking the store's print method:");
        System.out.println();

        store.print();

        System.out.println();
        System.out.println("Searching for client whose ID is 1...");
        System.out.println();

        Client foundClient = store.find(1);

        System.out.println("Printing the client whose ID is 4: ");
        System.out.println();

        foundClient.print();

        System.out.println();
        System.out.println("Searching for client whose ID is 4...");
        System.out.println();

        Client notFoundClient = store.find(4);

        System.out.println("Printing the client whose ID is 1: ");
        System.out.println();

        System.out.println(notFoundClient);
    }

}
