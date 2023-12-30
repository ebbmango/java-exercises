public class Client {
    public static int count; // this will default to 0, since it is not initialized
    private int clientId;
    private String clientName;

    // (Default) Constructor
    public Client() {
        this.clientName = "empty";
        System.out.println("Default constructor called. Client ID: " + clientId + " / Client's name: " + clientName);
    }

    public Client(String name) {
        this.clientName = name;
        System.out.println("Default constructor called. Client ID: " + clientId + " / Client's name: " + clientName);
    }

    // Initialization block
    {
        count++;
        this.clientId = count;
        System.out.println("Initialization block called. Count: " + count);
    }

    // Methods

    static int countClients() {
        return count;
    }

    public String getName() {
        return clientName;
    }

    public int getID() {
        return clientId;
    }

    public void print() {
        System.out.println("Client ID: " + clientId + " / Client's name: " + clientName);
    }
}