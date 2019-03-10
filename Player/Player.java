package Player;

import Physics.*;
import Weapon.Projectile;

import java.awt.*;

public class Player implements Drawable, Movable {

    private Ship ship;
    private int score;
    public final KeyInputSet input;
    private Vector velocity;
    private boolean strafing;
    private int timeSinceLastFire;

    public Player(Ship ship, KeyInputSet input, boolean strafing) {
        this.ship = ship;
        score = 0;
        this.input = input;
        this.strafing = strafing;
        this.timeSinceLastFire = -1;
        velocity = new Vector(0, 0);
    }

    public void setVelocity(Vector velocity) {
        ship.setVelocity(velocity);
    }

    public void addVelocity(Vector velocity) {
        ship.addVelocity(velocity);
    }

    private void turn(int degCW) {
        ship.turn(degCW);
    }

    public void turnLeft() {
        turn(-Ship.TURNING_SPEED);
    }

    public void turnRight() {
        turn(Ship.TURNING_SPEED);
    }

    public int getRotation() {
        return ship.getRotation();
    }

    public void zeroVelocity() {
        velocity = new Vector(0, 0);
    }

    public void moveForward() {
        if (velocity.getY() < Ship.MOVEMENT_SPEED)
            velocity.add(new Vector(0, Ship.MOVEMENT_SPEED));
    }

    public void moveBackward() {
        if (velocity.getY() > -Ship.MOVEMENT_SPEED)
            velocity.add(new Vector(0, -Ship.MOVEMENT_SPEED));
    }

    public void strafeRight() {
        if (velocity.getX() < Ship.MOVEMENT_SPEED)
            velocity.add(new Vector(Ship.MOVEMENT_SPEED, 0));
    }

    public void strafeLeft() {
        if (velocity.getX() > -Ship.MOVEMENT_SPEED)
            velocity.add(new Vector(-Ship.MOVEMENT_SPEED, 0));
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
        return new Vector(speed * Math.cos(Math.toRadians(rotation)), speed * Math.sin(Math.toRadians(rotation)));
    }

    private static Vector getBackwardMovement(int speed, int rotation) {
        return Player.getForwardMovement(speed, rotation + 180);
    }

    // TODO remove this after debugging is complete
    public Ship getShip() {
        return ship;
    }

    public void move() {

        Vector copy;
        if (strafing) {
            copy = new Vector(velocity);
        } else {
            copy = getForwardMovement(velocity.getY(), ship.getRotation());
        }

        copy.invertY();
        ship.setVelocity(copy);
        ship.move();
    }

    public void draw(Graphics g) {
        ship.draw(g);
        // TODO draw more things, like health? or is that part of the ship?
    }

    public Projectile fire(int weapon) {
        Projectile potentialProjectile = ship.fire(weapon);
        if (timeSinceLastFire >= potentialProjectile.getReload()) {
            // Allowed to shoot
            timeSinceLastFire = 0;
            return ship.fire(weapon);
        }
        return null;
    }

    public void updateTimeSinceLastFire(int amount) {
        if (timeSinceLastFire < 0)
            timeSinceLastFire = amount;
        else
            timeSinceLastFire += amount;
    }

    public void updateTimeSinceLastDamage(int amount) {
        ship.updateTimeSinceLastDamage(amount);
    }

    public Hitbox getHitbox() {
        return ship.getHitbox();
    }

    public boolean isInvulnerable() {
        return ship.isInvulnerable();
    }

}
