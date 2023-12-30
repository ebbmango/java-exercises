import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageGenerator {
  public static void createChargeDistributionImage(Charges charges, String filePath, int width, int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();

    // Clear the background
    g2d.setColor(Color.white);
    g2d.fillRect(0, 0, width, height);

    // Drawing the X and Y axes
    g2d.setColor(Color.black);
    g2d.drawLine(0, height / 2, width, height / 2); // This draws the X axis
    g2d.drawLine(width / 2, 0, width / 2, height); // This draws the Y axis

    // Draw grid lines
    g2d.setColor(Color.lightGray);

    final int SCALING_FACTOR = 100; // (*)

    // This draws vertical grid lines
    for (int x = 1; x < width; x += SCALING_FACTOR) {
      g2d.drawLine(x, 0, x, height);
    }

    // This draws horizontal grid lines
    for (int y = 1; y < height; y += SCALING_FACTOR) {
      g2d.drawLine(0, y, width, y);
    }

    for (Charge charge : charges.getCharges()) {
      // This sets up the cursor's position
      int x = (int) charge.getPositionX() * SCALING_FACTOR + width / 2;
      int y = (int) charge.getPositionY() * SCALING_FACTOR + height / 2;

      // This sets up the circle's size...
      double chargeValue = charge.getValue();
      int chargeSize = (int) (Math.abs(chargeValue) * 10e6); // (**)

      // ...And color
      Color chargeColor = (chargeValue < 0) ? Color.BLUE : Color.RED;
      g2d.setColor(chargeColor);

      // Finally, this draws the circles
      g2d.fillOval(x - chargeSize / 2, y - chargeSize / 2, chargeSize, chargeSize); // (***)
    }

    // This releases system resources associated with the graphics context.
    g2d.dispose();

    try {
      // And here we create the image file
      ImageIO.write(image, "PNG", new File(filePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

// (*)
// This scaling factor is important because of the image's scaling: each meter
// is a point in the width x height resolution of the image, this means that a
// distance of 2 meters between charges will translate to a difference of two
// pixels between the center points of each charge. Since the charges themselves
// are bigger in radius than that, they would all overlap and in the end none of
// them would be visible. This scaling factor makes sure that we can more
// clearly set them apart by multiplying the distances by 100, effectively
// making it so that each meter corresponds to 100 pixels in the scale.

// (**)
// It is also important to multiply the charge's size by a factor of 10e6
// because since their values were given in micro-coulombs but read in coulombs,
// their printed size would be 10e-6 of a pixel, which is effectively
// invisibile. By multiplying them by a factor of 10e6, we make it so that their
// sizes go back to corresponding to the canvas' base units.

// (***)
// The subtraction of the charge size divided by two is to centralize the oval
// at the charge's X and Y coordinates
