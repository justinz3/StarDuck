package Helpers;

import Physics.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Helpers {

    public static Image getImage(String fileAddress) {
        return new ImageIcon(fileAddress).getImage();
    }

    // Copied from: https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static Image rotateImage(Image img, double radians) {
        // Copied from: https://stackoverflow.com/questions/8639567/java-rotating-images

        BufferedImage image = toBufferedImage(img);

        // Rotation information

        double rotationRequired = radians;
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        return op.filter(image, null);
    }

    public static double getAngle(Vector currentPosition, Vector targetPosition) {
        Vector displacement = new Vector(targetPosition);
        displacement.add(Vector.scalarMult(currentPosition, -1));
        return Math.atan2(displacement.getY(), displacement.getX());
    }
}
