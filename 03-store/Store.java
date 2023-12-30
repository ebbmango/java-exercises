import java.util.ArrayList;
import java.util.List;

public class Store {
    private List<Client> clients = new ArrayList<>();

    // Methods
    public void add(Client aClient) {
        clients.add(aClient);
    }

    public void print() {
        for (Client client : clients) {
            client.print();
        }
    }

    public Client find(int ID) {
        for (Client client : clients) {
            if (client.getID() == ID) {
                return client;
            }
        }
        return null;
    }
}
