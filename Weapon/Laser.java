package Weapon;

import Helpers.Helpers;
import Physics.*;
import Player.Ship;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Laser extends Projectile implements Drawable {

    private Image laserGraphics = Helpers.getImage("graphics/laser.png");
    private BufferedImage bufferedLaser = Helpers.toBufferedImage(laserGraphics);
    private static Vector hitboxDisplacement = new Vector(-37, -40);
    private static int laserWidth = 60, laserHeight = 27;

    public Laser(Vector currentPosition, Vector targetPosition, int team) {
        super(currentPosition, targetPosition, 1, 1, -1, -1, 2,  Laser.getHitbox(currentPosition, Helpers.getAngle(currentPosition, targetPosition)), team);
        double angle = Helpers.getAngle(currentPosition, targetPosition);
        laserGraphics = Helpers.rotateImage(laserGraphics, angle);
        this.setReload(500);
    }

    private static Hitbox getHitbox(Vector currentPosition, double angle) {
        Hitbox hitbox = new Hitbox();
        MovableRectangle rect = new MovableRectangle(currentPosition, laserWidth, laserHeight);
        rect.setCenter(currentPosition);
        rect.setRotation((int) Math.toDegrees(angle));
        hitbox.add(rect); // TODO fix random values
        return hitbox;
    }

    public void onImpact(Hittable other) {
        if (other instanceof Ship) {
            Ship ship = (Ship) other;
            ship.takeDamage(getDamage());
        }
    }

    public void draw(Graphics g) {
        g.drawImage(laserGraphics, (int) (getPosition().getX() + hitboxDisplacement.getX()),
                (int) (getPosition().getY() + hitboxDisplacement.getY()), null);
        getHitbox().draw(g);
    }

    public Dimension getImageSize() {
        return new Dimension(bufferedLaser.getWidth(), bufferedLaser.getHeight());
    }
}
