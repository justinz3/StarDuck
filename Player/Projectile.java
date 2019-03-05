// Description: A Projectile that will move and inflict damage
// Created: 3/2/19

package Player;

import Physics.Drawable;
import Physics.Vector;

import javax.swing.*;
import java.awt.*;

public abstract class Projectile implements Drawable {

    private int damage; // healing can be negative damage
    private int health;
    private int timeCreated;
    private int fuse;
    private Vector targetPosition;
    private Vector position, velocity;
    private int speed;

    public abstract void onImpact(); // what the projectile does when it hits something

    public abstract void draw(Graphics g, JPanel panel);

    public void move() {
        position.add(velocity);
    }


}
