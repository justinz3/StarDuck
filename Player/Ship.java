// Description: A Ship that can be controlled by a Player
// Created: 3/2/19

package Player;

import Physics.*;
import Weapon.*;
import Helpers.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ship implements Drawable {

    private Hitbox hitbox;
    private Vector position, velocity, acceleration;
    private Image gif;
    private BufferedImage bufferedImage;
    private int health;
    private Weapon weapon;
    private int rotation;
    private final int initalRotation = 90; // ship starts off facing up
    private Dimension screenSize;

    public static final int MOVEMENT_SPEED = 2;
    public static final int TURNING_SPEED = 4;

    public Ship() {
        this(new Vector(100, 100), new Vector(0, 0), new Vector(0, 0), new Weapon(new Laser(new Vector(), new Vector())), "graphics/Arwing-Blue.gif", new Dimension(1572, 912));
    }

    public Ship(Dimension screenSize) {
        this(new Vector(100, 100), new Vector(0, 0), new Vector(0, 0), new Weapon(new Laser(new Vector(), new Vector())), "graphics/Arwing-Blue.gif", screenSize);
    }

    public Ship(Vector position, Vector velocity, Vector acceleration, Weapon weapon, String iconAddress, Dimension screensize) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.weapon = weapon;
        rotation = initalRotation;
        gif = new ImageIcon(iconAddress).getImage();
        try {
            this.bufferedImage = ImageIO.read(new File(iconAddress));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(Helpers.rotateImage(gif, Math.toRadians(-rotation + initalRotation)), (int) position.getX(), (int) position.getY(), null);
    }

    public void move() {
        Vector tempPosition = new Vector(position);
        tempPosition.add(velocity);

        if (tempPosition.getX() >= 0 && tempPosition.getX() <= screenSize.width - bufferedImage.getWidth())
            position.add(new Vector(velocity.getX(), 0));
        if (tempPosition.getY() >= 0 && tempPosition.getY() <= screenSize.height - bufferedImage.getHeight())
            position.add(new Vector(0, velocity.getY()));

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
