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
        this.setReload(500);
    }

    private static Hitbox getHitbox(Vector currentPosition, double angle) {
        Hitbox hitbox = new Hitbox();
        MovableRectangle rect = new MovableRectangle(currentPosition, bombWidth, bombHeight);
        rect.setCenter(currentPosition);
        rect.setRotation((int) Math.toDegrees(angle));
        hitbox.add(rect);
        return hitbox;
    }

    private void setExploded() {
        if(exploded)
            return;
        poison = Integer.MAX_VALUE / 100;
        setVelocity(new Vector(0, 0));
        exploded = true;
        setDamage(0);
        setHealth(Integer.MAX_VALUE);
    }

    public void onImpact(Hittable other) {
        setExploded();

        if(other != null) {
            other.takeDamage(getDamage());
            return;
        }

        Weapon explosion = new Weapon(new Laser(getTeam()), getTeam());

        for(int i = 0; i < numLasers; i++) {
            double radians = (2 * Math.PI) / numLasers * i;

            GamePanel.toBeAdded.add(explosion.fire(getPosition(), radians));
        }
    }

    public boolean impact(Hittable other) {

        boolean result = super.impact(other);
        setExploded();

        return result;
    }

    public void draw(Graphics g) {
        g.drawImage(Helpers.rotateImage(Helpers.getImage((exploded ? "graphics/missile-explosion.gif" : "graphics/missile-countdown.gif")), angle), (int) (getPosition().getX() + hitboxDisplacement.getX()),
                (int) (getPosition().getY() + hitboxDisplacement.getY()), null);
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
