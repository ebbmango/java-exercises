import java.lang.Math;

public class Vector {

  private final double componentX;
  private final double componentY;
  private final double componentZ;

  // CONSTRUCTORS:

  // Constructor to initialize by components
  public Vector(double componentX, double componentY, double componentZ) {
    this.componentX = componentX;
    this.componentY = componentY;
    this.componentZ = componentZ;
  }

  // Constructor to initialize by magnitude and a VECTOR unit vector
  public Vector(double magnitude, Vector unitVector) {
    if (unitVector == null) {
      throw new IllegalArgumentException("unitVector cannot be null.");
    }
    this.componentX = unitVector.getComponentX() * magnitude;
    this.componentY = unitVector.getComponentY() * magnitude;
    this.componentZ = unitVector.getComponentZ() * magnitude;
  }

  // Constructor to initialize by magnitude and a unit vector's components
  public Vector(double magnitude, double unitVectorXcomponent, double unitVectorYcomponent,
      double unitVectorZcomponent) {
    this.componentX = unitVectorXcomponent * magnitude;
    this.componentY = unitVectorYcomponent * magnitude;
    this.componentZ = unitVectorZcomponent * magnitude;
  }

  // METHODS:

  // getters:

  public double getMagnitude() {
    return Math.sqrt(Math.pow(componentX, 2) + Math.pow(componentY, 2) + Math.pow(componentZ, 2));
  }

  public Vector getUnitVector() {
    double magnitude = getMagnitude();
    return new Vector(componentX / magnitude, componentY / magnitude, componentZ / magnitude);
  }

  public double getComponentX() {
    return componentX;
  }

  public double getComponentY() {
    return componentY;
  }

  public double getComponentZ() {
    return componentZ;
  }

  // other methods:

  static public Vector dotProduct(Vector v1, Vector v2) {
    return new Vector(v1.getComponentX() * v2.getComponentX(), v1.getComponentY() * v2.getComponentY(),
        v1.getComponentZ() * v2.getComponentZ());
  }

  public static Vector addition(Vector v1, Vector v2) {
    double resultX = v1.getComponentX() + v2.getComponentX();
    double resultY = v1.getComponentY() + v2.getComponentY();
    double resultZ = v1.getComponentZ() + v2.getComponentZ();
    return new Vector(resultX, resultY, resultZ);
  }

}
