/* File Example00.java */

public class FreeFall {
  // Gravitational Acceleration Constant
  static final double ACCELERATION = -9.81;

  public static void main(String[] args) {
    // args[0] = initial height (in meters);
    // args[1] = initial speed (in km/h);
    // args[2] = time (in seconds);

    // Getting the variables from the user and adequately formatting them
    double initialHeight = Double.parseDouble(args[0]);
    double initialSpeedKm = Double.parseDouble(args[1]);
    double initialSpeedMt = Double.parseDouble(args[1]) * 1000.0 / 3600.0;
    double time = Double.parseDouble(args[2]);

    // Solving for the position:
    double position = (ACCELERATION * (time * time)) / 2 + (initialSpeedMt * time) + initialHeight;

    String result = String.format(
        "The position at time %.2fs of the stone that was thrown downwards from the top of a tower at height %.2fm with a initial speed of %.2fkm/h is:\n%.2fm",
        time, initialHeight, initialSpeedKm, initialHeight - (position - initialHeight));

    System.out.print(result + "\n");
  }
}

// I just realized after completing the code that the stone was thrown upwards instead of downwards.
// I shall correct this later, once this class is (thankfully) not graded.
