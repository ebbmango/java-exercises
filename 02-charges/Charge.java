public class Charge {

  public static final double COULOMBS_CONSTANT = 8.988e9; // N·m^2/C^2

  private double value;
  private double positionX;
  private double positionY;
  private double positionZ;

  public Charge(double value, double positionX, double positionY, double positionZ) {
    this.value = value;
    this.positionX = positionX;
    this.positionY = positionY;
    this.positionZ = positionZ;
  }

  // METHODS:

  // getters:

  public double getValue() {
    return value;
  }

  public double getPositionX() {
    return positionX;
  }

  public double getPositionY() {
    return positionY;
  }

  public double getPositionZ() {
    return positionZ;
  }

  // setters:

  public void setValue(double value) {
    this.value = value;
  }

  public void setPositionX(double positionX) {
    this.positionX = positionX;
  }

  public void setPositionY(double positionY) {
    this.positionY = positionY;
  }

  public void setPositionZ(double positionZ) {
    this.positionZ = positionZ;
  }

  // other methods:

  // This method computes the electric field created by this charge at a specified
  // point in space, characterized by its X, Y, and Z coordinates.
  public Vector fieldAtPoint(double x, double y, double z) {

    // Calculate the distance from the charge to the given point using a Vector
    Vector distanceVector = new Vector(x - positionX, y - positionY, z - positionZ);
    double r = distanceVector.getMagnitude();

    if (r == 0) {
      // Special case: the point is the same as the charge, electric field is zero.
      return new Vector(0, 0, 0);
    }

    // With this, we can calculate the field's magnitude and direction:
    double fieldMagnitude = (COULOMBS_CONSTANT * value) / Math.pow(r, 2);
    Vector fieldDirection = distanceVector.getUnitVector();

    return new Vector(fieldMagnitude, fieldDirection);
  }

  // This method determines the force experienced by this charge due to the
  // influence of another charge, which is provided as a parameter.
  public Vector forceByCharge(Charge charge) {
    Vector distanceVector = new Vector(charge.getPositionX() - positionX, charge.getPositionY() - positionY,
        charge.getPositionZ() - positionZ);
    double r = distanceVector.getMagnitude();

    if (r == 0) {
      // Special case: the charges are at the same position; force is zero.
      return new Vector(0, 0, 0);
    }

    double forceMagnitude = (COULOMBS_CONSTANT * Math.abs(value * charge.getValue())) / Math.pow(r, 2);
    Vector forceDirection = distanceVector.getUnitVector();

    return new Vector(forceMagnitude, forceDirection);
  }

}
