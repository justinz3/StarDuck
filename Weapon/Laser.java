package Weapon;

import Helpers.Helpers;
import Physics.Drawable;
import Physics.Vector;
import Player.Ship;

import javax.swing.*;
import java.awt.*;

public class Laser extends Projectile implements Drawable {

    private static final Image laserGraphics = Helpers.getImage("graphics/laser.png");

    public Laser(Vector currentPosition, Vector targetPosition) {
        super(currentPosition, targetPosition, 1, 1,-1, -1, 2);
    }

    public void onImpact(Drawable other) {
        if(other instanceof Ship) {
            Ship ship = (Ship) other;
            ship.takeDamage(getDamage());
        }
    }

    public void draw(Graphics g) {
        //System.out.println(getPosition());
        g.drawImage(laserGraphics, (int) getPosition().getX(), (int) getPosition().getY(), null);
    }
}
