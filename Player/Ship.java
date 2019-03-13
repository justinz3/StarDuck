// Description: A Ship that can be controlled by a Player
// Created: 3/2/19

package Player;

import Panels.GamePanel;
import Physics.*;
import Physics.MovableRectangle;
import Physics.MovableShape;
import Weapon.*;
import Helpers.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ship extends Interactable {

    private Vector position, velocity, acceleration;
    private Image gif;
    private BufferedImage bufferedImage;
    private Weapon primary, secondary;
    private int rotation;
    private final int initalRotation = 90; // ship starts off facing up
    private Dimension screenSize;
    private ShipType shipType;
    private int timeSinceLastDamage;
    private boolean invulnerable = false, drewShipLastTime = false;

    private static final int INVULNERABILITY_DURATION = 1500; // In milliseconds
    static final int MOVEMENT_SPEED = 2;
    static final int TURNING_SPEED = 1;
    private static final int SHIP_HEALTH = 5;
    private static final int SHIP_DAMAGE = 2;

    public enum ShipType {
        RED("graphics/Arwing-Red.gif", new MovableRectangle(new Vector(97, 120), 20, 40), new MovableRectangle(new Vector(132, 140), 90, 20)),
        GREEN("graphics/Arwing-Green.gif", new MovableRectangle(new Vector(90, 120), 15, 40), new MovableRectangle(new Vector(124, 130), 82, 20)),
        BLUE("graphics/Arwing-Blue.gif", new MovableRectangle(new Vector(105, 120), 20, 40), new MovableRectangle(new Vector(145, 140), 100, 20));

        private String iconAddress;
        private Hitbox hitbox, originalHitbox;

        ShipType(String iconAddress, MovableShape rect1, MovableShape rect2) {
            this.iconAddress = iconAddress;
            this.hitbox = new Hitbox();
            hitbox.add(rect1);
            hitbox.add(rect2);

            this.originalHitbox = new Hitbox();
            originalHitbox.add(new MovableRectangle((MovableRectangle) rect1));
            originalHitbox.add(new MovableRectangle((MovableRectangle) rect2));
        }

        public String toString() {
            return Character.toString(name().charAt(0)).toUpperCase() + name().substring(1).toLowerCase();
        }
    }

    public Ship() {
        this(new Dimension(1572, 912));
    }

    public Ship(Dimension screenSize) {
        this(ShipType.BLUE, screenSize, 0);
    }

    public Ship(ShipType shipType, Dimension screenSize, int team) {
        this(new Vector(100, 100), new Vector(0, 0), new Vector(0, 0), new Laser(team), new Bomb(team), shipType, screenSize, team);
    }

    public Ship(Vector position, Vector velocity, Vector acceleration, Projectile primary, Projectile secondary, ShipType shipType, Dimension screensize, int team) {
        super(shipType.hitbox, SHIP_HEALTH, SHIP_DAMAGE, team);

        this.position = shipType.hitbox.getCenter(); // tie the hitbox and projectile positions together
        this.position.setX(position.getX());
        this.position.setY(position.getY());
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.primary = new Weapon(primary, team);
        this.secondary = new Weapon(secondary, team);
        rotation = initalRotation;
        gif = new ImageIcon(shipType.iconAddress).getImage();
        try {
            this.bufferedImage = ImageIO.read(new File(shipType.iconAddress));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        this.shipType = shipType;
        this.screenSize = screensize;
        this.timeSinceLastDamage = -1;
        updateCenter();
    }

    public void draw(Graphics g) {
        if (timeSinceLastDamage >= INVULNERABILITY_DURATION && invulnerable) {
            invulnerable = false;
            drewShipLastTime = false;
        }

        if (invulnerable) {
            if (!drewShipLastTime)
                g.drawImage(Helpers.rotateImage(gif, Math.toRadians(-rotation + initalRotation)), (int) position.getX(), (int) position.getY(), null);

            drewShipLastTime = !drewShipLastTime;
        } else {
            g.drawImage(Helpers.rotateImage(gif, Math.toRadians(-rotation + initalRotation)), (int) position.getX(), (int) position.getY(), null);
        }

        g.setColor(Color.WHITE);
        g.drawString(String.format("%.0f HP", getHealth()), (int) position.getX(), (int) position.getY());
        g.drawString(String.format("%.0f Pts", GamePanel.playerScores.get(getTeam())), (int) (position.getX()), (int) (position.getY() + getHeight()));
    }

    public void move() {
        Vector tempPosition = new Vector(position);
        tempPosition.add(velocity);

        if (tempPosition.getX() >= bufferedImage.getWidth() / 8 && tempPosition.getX() <= screenSize.width - 1.5 * bufferedImage.getWidth())
            position.add(new Vector(velocity.getX(), 0));
        if (tempPosition.getY() >= bufferedImage.getHeight() / 8 && tempPosition.getY() <= screenSize.height - 1.5 * bufferedImage.getHeight())
            position.add(new Vector(0, velocity.getY()));

        velocity.add(acceleration);
        for (int index = 0; index < shipType.hitbox.size(); index++) {
            MovableRectangle originalRect = (MovableRectangle) shipType.originalHitbox.get(index);
            Vector relativePosition = new Vector(originalRect.getPosition());
            relativePosition.add(position);
            relativePosition.add(new Vector(-originalRect.getWidth() / 2.0, -originalRect.getHeight() / 2.0)); // Move position to be relative to the center of the Ship
            relativePosition.add(new Vector(-1 * bufferedImage.getWidth() / 2.0, -1 * bufferedImage.getHeight() / 2.0)); // Move position to be relative to the top left corner of the Ship
            shipType.hitbox.get(index).setPosition(relativePosition);
        }

        velocity = new Vector(0, 0);

        shipType.hitbox.setRotation(-rotation + initalRotation);
        updateCenter();
    }

    public Projectile fire(int weapon) {
        Vector frontDisplacement = new Vector(0, -bufferedImage.getHeight() / 2);
        frontDisplacement.rotate(Math.toRadians(-rotation + initalRotation));
        return (weapon == 0 ? primary : secondary).fire(new Vector(getCenter().getX() + frontDisplacement.getX(), getCenter().getY() + frontDisplacement.getY()), Math.toRadians(-rotation));
    }

    public void turn(int degCCW) {
        rotation -= degCCW;
    }

    public int getRotation() {
        return rotation;
    }

    private void updateCenter() {
        shipType.hitbox.setCenter(getCenter());
    }

    private Vector getCenter() {
        return new Vector(position.getX() + bufferedImage.getWidth() / 2, position.getY() + bufferedImage.getHeight() / 2);
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

    public Hitbox getHitbox() {
        return shipType.hitbox;
    }

    public void updateTimeSinceLastDamage(int amount) {
        if (timeSinceLastDamage < 0)
            timeSinceLastDamage = amount;
        else
            timeSinceLastDamage += amount;
    }

    public int getTimeSinceLastDamage() {
        return timeSinceLastDamage;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    private void becomeInvulnerable() {
        invulnerable = true;
        drewShipLastTime = false;
    }

    @Override
    public boolean takeDamage(double damage) {
        if (timeSinceLastDamage >= INVULNERABILITY_DURATION) {
            setHealth(getHealth() - damage);
            timeSinceLastDamage = 0;

            Helpers.addPoints(getTeam(), -5);

            if (getHealth() > 0)
                becomeInvulnerable();
        }

        return getHealth() <= 0;
    }

    public String getColor() {
        return shipType.toString();
    }

    public double getWidth() {
        return bufferedImage.getWidth();
    }

    public double getHeight() {
        return bufferedImage.getHeight();
    }
}
