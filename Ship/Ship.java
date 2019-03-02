// Description: A Ship that can be controlled by a Player
// Created: 3/2/19

package Ship;

import Physics.*;

import javax.swing.*;
import java.awt.*;

public class Ship implements Drawable {

    private Hitbox hitbox;
    private Vector position, velocity, acceleration;
    private Image gif;

    public Ship() {
        this(new Vector(100, 100), new Vector(10, 10), new Vector(0, 0), "gifs/Arwing-Blue.gif");
    }

    public Ship(Vector position, Vector velocity, Vector acceleration, String iconAddress) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        gif = new ImageIcon(iconAddress).getImage();
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(gif, (int) position.getX(), (int) position.getY(), panel);
    }

    public void move() {
        position.add(velocity);
        velocity.add(acceleration);
    }

    // Getters and Setters
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void addVelocity(Vector velocity) {
        this.velocity.add(velocity);
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void addAcceleration(Vector acceleration) {
        this.acceleration.add(acceleration);
    }

}
