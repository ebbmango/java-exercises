import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Charges {

  // This is just a reference to a list, but doesn't point to any actual list
  // object in memory.
  private List<Charge> charges;

  // CONSTRUCTORS:

  // Constructor to initialize by quantity
  public Charges(int quantity) {
    charges = new ArrayList<>(quantity);
    for (int i = 0; i < quantity; i++) {
      charges.add(new Charge(0, 0, 0, 0));
    }
  }

  // Constructor to initialize by list
  public Charges(List<Charge> chargeList) {
    charges = new ArrayList<>(chargeList);
  }

  // METHODS:

  // getters:

  public List<Charge> getCharges() {
    return charges;
  }

  public double getTotalCharge() {
    double totalCharge = 0;
    for (Charge charge : charges) {
      totalCharge += charge.getValue();
    }
    return totalCharge;
  }

  // setters or otherwise related methods:

  public void addCharge(Charge charge) {
    charges.add(charge);
  }

  // printing methods:

  public void printCharges() {
    for (int i = 0; i < charges.size(); i++) {
      Charge charge = charges.get(i);
      System.out.println("[" + i + "] - Charge: " + charge.getValue() + "C / Position: X = " + charge.getPositionX()
          + "; Y = " + charge.getPositionY() + "; Z = " + charge.getPositionZ() + ";");
    }
  }

  public void printForces() {
    for (int i = 0; i < charges.size(); i++) {
      Vector force = totalForceAtCharge(i);
      System.out.println("Total force acting on charge [" + i + "]: " + force.getMagnitude() + "N / Direction: X = "
          + force.getComponentX() + "; Y = " + force.getComponentY() + "; Z = " + force.getComponentZ() + ";");
    }
  }

  // additional methods:

  // This method computes the combined electric field produced by all the charges
  // at a specific point in space, defined by its X, Y, and Z coordinates.
  public Vector totalFieldAtPoint(double x, double y, double z) {

    Vector sumFields = new Vector(0, 0, 0);

    for (Charge charge : charges) {
      Vector chargeField = charge.fieldAtPoint(x, y, z);
      sumFields = Vector.addition(sumFields, chargeField);
    }

    return sumFields;
  }

  public Vector totalForceAtCharge(int chargeIndex) {

    // This takes care of things in case the user has inputted an invalid index just
    // as much as if the list in itself is empty
    if (chargeIndex < 0 || chargeIndex >= charges.size()) {
      throw new IllegalArgumentException("Invalid chargeIndex.");
    }

    Vector sumForces = new Vector(0, 0, 0);

    for (int i = 0; i < charges.size(); i++) {
      if (i == chargeIndex) {
        continue;
      }
      Vector force = charges.get(chargeIndex).forceByCharge(charges.get(i));
      sumForces = Vector.addition(sumForces, force);
    }

    return sumForces;
  }

}
