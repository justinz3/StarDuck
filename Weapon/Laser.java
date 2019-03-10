package Weapon;

import Helpers.Helpers;
import Physics.*;
import Player.Ship;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Laser extends Projectile implements Drawable {

    private Image laserGraphics = Helpers.getImage("graphics/laser.png");
    private BufferedImage bufferedLaser = Helpers.toBufferedImage(laserGraphics);

    public Laser(Vector currentPosition, Vector targetPosition) {
        super(currentPosition, targetPosition, 1, 1, -1, -1, 2,
                Laser.getHitbox(currentPosition, Helpers.getAngle(currentPosition, targetPosition)));
        double angle = Helpers.getAngle(currentPosition, targetPosition);
        laserGraphics = Helpers.rotateImage(laserGraphics, angle);
        this.setReload(500);
    }

    private static Hitbox getHitbox(Vector currentPosition, double angle) {
        Hitbox hitbox = new Hitbox();
        MovableRectangle rect = new MovableRectangle(currentPosition, 50, 20);
        rect.setCenter(currentPosition);
        rect.setRotation((int) Math.toDegrees(angle));
        hitbox.add(rect); // TODO fix random values
        return hitbox;
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
        getHitbox().draw(g);
    }

    public Dimension getImageSize() {
        return new Dimension(bufferedLaser.getWidth(), bufferedLaser.getHeight());
    }
}
