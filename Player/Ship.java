// Description: A Ship that can be controlled by a Player
// Created: 3/2/19

package Player;

import Physics.*;

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
    private Dimension screenSize;

    public Ship() {
        this(new Vector(100, 100), new Vector(0, 0), new Vector(0, 0), /*new Weapon(),*/ "graphics/Arwing-Blue.gif", new Dimension(1572, 912));
    }

    public Ship(Vector position, Vector velocity, Vector acceleration, /*Weapon weapon,*/ String iconAddress, Dimension screenSize) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.screenSize = screenSize;
        /*this.weapon = weapon;*/
        gif = new ImageIcon(iconAddress).getImage();
        try {
            this.bufferedImage = ImageIO.read(new File(iconAddress));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(gif, (int) position.getX(), (int) position.getY(), panel);
    }

    public void move() {
        Vector tempPosition = new Vector(position);
        tempPosition.add(velocity);

        System.out.println(screenSize);
        if (tempPosition.getX() >= 0 && tempPosition.getX() <= screenSize.width - bufferedImage.getWidth())
            position.add(new Vector(velocity.getX(), 0));
        if (tempPosition.getY() >= 0 && tempPosition.getY() <= screenSize.height - bufferedImage.getHeight())
            position.add(new Vector(0, velocity.getY()));

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

}
