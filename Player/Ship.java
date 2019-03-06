// Description: A Ship that can be controlled by a Player
// Created: 3/2/19

package Player;

import Helpers.Helpers;
import Physics.*;
import Weapon.*;

import javax.swing.*;
import java.awt.*;

public class Ship implements Drawable {

    private Hitbox hitbox;
    private Vector position, velocity, acceleration;
    private Image gif;
    private int health;
    private Weapon weapon;
    private int rotation;
    private final int initalRotation = 90; // ship starts off facing up

    public static final int MOVEMENT_SPEED = 2;
    public static final int TURNING_SPEED = 4;

    public Ship() {
        this(new Vector(100, 100), new Vector(0, 0), new Vector(0, 0),
                new Weapon(new Laser(new Vector(), new Vector())), "graphics/Arwing-Blue.gif");
    }

    public Ship(Vector position, Vector velocity, Vector acceleration, Weapon weapon, String iconAddress) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.weapon = weapon;
        rotation = initalRotation;
        gif = new ImageIcon(iconAddress).getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(Helpers.rotateImage(gif, Math.toRadians(-rotation + initalRotation)), (int) position.getX(), (int) position.getY(), null);
    }

    public void move() {
        position.add(velocity);
        velocity.add(acceleration);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public void turn(int degCCW) {
        rotation -= degCCW;
    }

    public int getRotation() {
        return rotation;
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

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
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

    public Weapon getWeapon() {
        return weapon;
    }

}
