package Weapon;

import Helpers.Helpers;
import Physics.Drawable;
import Physics.Vector;
import Player.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Laser extends Projectile implements Drawable {

    private Image laserGraphics = Helpers.getImage("graphics/laser.png");
    private BufferedImage bufferedLaser = Helpers.toBufferedImage(laserGraphics);

    public Laser(Vector currentPosition, Vector targetPosition) {
        super(currentPosition, targetPosition, 1, 1, -1, -1, 2);
        Vector displacement = new Vector(targetPosition);
        displacement.add(Vector.scalarMult(currentPosition, -1));
        double angle = Math.atan2(displacement.getY(), displacement.getX());
        laserGraphics = Helpers.rotateImage(laserGraphics, angle);

    }

    public void onImpact(Drawable other) {
        if (other instanceof Ship) {
            Ship ship = (Ship) other;
            ship.takeDamage(getDamage());
        }
    }

    public void draw(Graphics g) {
        //System.out.println(getPosition());
        g.drawImage(laserGraphics, (int) getPosition().getX(), (int) getPosition().getY(), null);
    }

    public Dimension getImageSize() {
        return new Dimension(bufferedLaser.getWidth(), bufferedLaser.getHeight());
    }
}
