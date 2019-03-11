package Weapon;

import Helpers.Helpers;
import Physics.*;
import Player.Ship;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Laser extends Projectile implements Drawable {

    private Image laserGraphics = Helpers.getImage("graphics/laser.png");
    private BufferedImage bufferedLaser = Helpers.toBufferedImage(laserGraphics);
    private static Vector hitboxDisplacement = new Vector(-37, -40); // TODO fix random values (find less "jank" way to do this)
    private static int laserWidth = 60, laserHeight = 27;

    public Laser(int team) {
        this(new Vector(), new Vector(), team);
    }

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
        hitbox.add(rect);
        return hitbox;
    }

    public void onImpact(Hittable other) {

        other.takeDamage(getDamage());
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
