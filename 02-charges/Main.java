import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    // Initializing the system of charges
    List<Charge> chargeList = new ArrayList<>();

    chargeList.add(new Charge(2e-6, 0, -1, 0));
    chargeList.add(new Charge(-2e-6, 0, 2, 0));
    chargeList.add(new Charge(1e-6, 1, -1, 0));
    chargeList.add(new Charge(-1e-6, 1, 1, 0));

    Charges charges = new Charges(chargeList);

    // Printing the values of the charges and the total charge in the system
    charges.printCharges();

    double totalCharge = charges.getTotalCharge();
    System.out.println("The total charge in the system is: " + totalCharge + "C.");

    // Generating the image file for the charges' system
    String filePath = "./charge_distribution.png";
    int width = 800;
    int height = 600;

    ImageGenerator.createChargeDistributionImage(charges, filePath, width, height);
  }
}
