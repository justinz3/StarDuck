package Player;

import Physics.Drawable;
import Physics.Vector;

import javax.swing.*;
import java.awt.*;

public class Player implements Drawable {

    private Ship ship;
    private int score;
    public final KeyInputSet input;
    public static final int MOVEMENT_SPEED = 2;
    public static final int TURNING_SPEED = 4;
    private int rotation;
    private Vector velocity;
    private boolean strafing;

    public Player(Ship ship, KeyInputSet input, boolean strafing) {
        this.ship = ship;
        score = 0;
        this.input = input;
        this.strafing = strafing;

        velocity = new Vector(0, 0);
    }

    public void setVelocity(Vector velocity) {
        ship.setVelocity(velocity);
    }

    public void addVelocity(Vector velocity) {
        ship.addVelocity(velocity);
    }

    private void turn(int degCW) {
        rotation += degCW;
    }

    public void turnLeft() {
        turn(-Player.TURNING_SPEED);
    }

    public void turnRight() {
        turn(Player.TURNING_SPEED);
    }

    public int getRotation() {
        return rotation;
    }

    public void moveForward() {
        if(velocity.getY() < MOVEMENT_SPEED)
            velocity.add(new Vector(0, MOVEMENT_SPEED));
    }

    public void moveBackward() {
        if(velocity.getY() > -MOVEMENT_SPEED)
            velocity.add(new Vector(0, -MOVEMENT_SPEED));
    }

    public void strafeRight() {
        if(velocity.getX() < MOVEMENT_SPEED)
            velocity.add(new Vector(MOVEMENT_SPEED, 0));
    }

    public void strafeLeft() {
        if(velocity.getX() > -MOVEMENT_SPEED)
            velocity.add(new Vector(-MOVEMENT_SPEED, 0));
    }

    public boolean isStrafing() {
        return strafing;
    }

    public void stopForward() {
        moveBackward();
    }

    public void stopBackward() {
        moveForward();
    }

    public void stopLeft() {
        strafeRight();
    }

    public void stopRight() {
        strafeLeft();
    }

    private static Vector getForwardMovement(double speed, int rotation) {
        return new Vector(speed * Math.cos(toRadians(rotation)), speed * Math.sin(toRadians(rotation)));
    }

    private static Vector getBackwardMovement(int speed, int rotation) {
        return Player.getForwardMovement(speed, rotation + 180);
    }

    public static double toRadians(int degrees) {
        return (double) degrees / 180 * Math.PI;
    }

    // TODO remove this after debugging is complete
    public Ship getShip() {
        return ship;
    }

    public void move() {

        Vector copy;
        if(strafing) {
            copy = new Vector(velocity);
        }
        else {
            copy = getForwardMovement(velocity.getY(), rotation);
        }

        //System.out.println("Before: " + ship.getVelocity());
        //.getVelocity().invertY(); // because y-axis is inverted because arrays (so negative direction is up)
        //System.out.println("After: " + ship.getVelocity());
        copy.invertY();
        ship.setVelocity(copy);
        ship.move();
    }

    public void draw(Graphics g, JPanel panel) {
        ship.draw(g, panel);
        // TODO draw more things
    }
}
