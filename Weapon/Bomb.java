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

    private BufferedImage bufferedBomb = Helpers.toBufferedImage(Helpers.getImage("graphics/missile-countdown.gif"));
    private static Vector hitboxDisplacement = new Vector(-60, -40);
    private static int bombWidth = 50, bombHeight = 35;
    private static int numLasers = 8;
    private double angle;
    private boolean exploded = false;
    private double poison = 0;

    public Bomb(int team) {
        this(new Vector(), new Vector(), team);
    }

    public Bomb(Vector currentPosition, Vector targetPosition, int team) {
        super(currentPosition, targetPosition, 3, 1, -1, 3700, 1,  Bomb.getHitbox(currentPosition, Helpers.getAngle(currentPosition, targetPosition)), team);
        angle = Helpers.getAngle(currentPosition, targetPosition);
        this.setReload(1000);
    }

    private static Hitbox getHitbox(Vector currentPosition, double angle) {
        Hitbox hitbox = new Hitbox();
        MovableRectangle rect = new MovableRectangle(currentPosition, bombWidth, bombHeight);
        rect.setCenter(currentPosition);
        rect.setRotation((int) Math.toDegrees(angle));
        hitbox.add(rect); // TODO fix random values
        return hitbox;
    }

    public void onImpact(Hittable other) {
        if(other != null) {
            other.takeDamage(getDamage());
            return;
        }

        Weapon explosion = new Weapon(new Laser(getTeam()), getTeam());

        for(int i = 0; i < numLasers; i++) {
            double radians = (2 * Math.PI) / numLasers * i;

            GamePanel.toBeAdded.add(explosion.fire(getPosition(), radians));
//            GamePanel.toBeDrawn.add(new Explosion(getPosition()));
        }

        poison = 0.01;
        setVelocity(new Vector(0, 0));
        exploded = true;
    }

    public void draw(Graphics g) {
        while(
        !g.drawImage(Helpers.rotateImage(Helpers.getImage((exploded ? "graphics/missile-explosion.gif" : "graphics/missile-countdown.gif")), angle), (int) (getPosition().getX() + hitboxDisplacement.getX()),
                (int) (getPosition().getY() + hitboxDisplacement.getY()), null)
        )
            continue;
        getHitbox().draw(g);
    }

    public Dimension getImageSize() {
        return new Dimension(bufferedBomb.getWidth(), bufferedBomb.getHeight());
    }

    public void move() {
        super.move();

        if(getPosition().equals(getTargetPosition()) && !exploded) {
            onImpact(null);
        }

        setHealth(getHealth() - poison);
    }
}