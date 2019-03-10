package Weapon;

import Helpers.Helpers;
import Panels.GamePanel;
import Physics.Hitbox;
import Physics.Hittable;
import Physics.MovableRectangle;
import Physics.Vector;
import Player.Ship;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bomb extends Projectile {

    private Image bombGraphics = Helpers.getImage("graphics/missile-countdown.gif");
    private BufferedImage bufferedBomb = Helpers.toBufferedImage(bombGraphics);
    private static Vector hitboxDisplacement = new Vector(-37, -40);
    private static int laserWidth = 60, laserHeight = 27;
    private static int numLasers = 8;

    public Bomb(int team) {
        this(new Vector(), new Vector(), team);
    }

    public Bomb(Vector currentPosition, Vector targetPosition, int team) {
        super(currentPosition, targetPosition, 3, 1, -1, 3000, 1,  Bomb.getHitbox(currentPosition, Helpers.getAngle(currentPosition, targetPosition)), team);
        double angle = Helpers.getAngle(currentPosition, targetPosition);
        bombGraphics = Helpers.rotateImage(bombGraphics, angle);
        this.setReload(5000);
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
        if(other != null)
            other.takeDamage(getDamage());

        Weapon explosion = new Weapon(new Laser(getTeam()), getTeam());

        for(int i = 0; i < numLasers; i++) {
            double radians = (2 * Math.PI) / numLasers * i;

            GamePanel.toBeAdded.add(explosion.fire(getPosition(), radians));
        }


    }

    public void draw(Graphics g) {
        g.drawImage(bombGraphics, (int) (getPosition().getX() + hitboxDisplacement.getX()),
                (int) (getPosition().getY() + hitboxDisplacement.getY()), null);
        getHitbox().draw(g);
    }

    public Dimension getImageSize() {
        return new Dimension(bufferedBomb.getWidth(), bufferedBomb.getHeight());
    }

    public void move() {
        super.move();

        if(getPosition().equals(getTargetPosition())) {
            setHealth(0);
            onImpact(null);
        }
    }
}
