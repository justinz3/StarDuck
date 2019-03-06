// Description: A Projectile that will move and inflict damage
// Created: 3/2/19

package Weapon;

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
    private double speed;
    private int reload;

    public Projectile(Vector currentPosition, Vector targetPosition, int damage, int health, int timeCreated, int fuse, double speed) {

        this.damage = damage;
        this.health = health;
        this.timeCreated = timeCreated;
        this.fuse = fuse;
        this.speed = speed;

        this.position = currentPosition;
        this.targetPosition = targetPosition;
        setVelocityFromTarget();
    }

    public Projectile(Vector currentPosition, Vector targetPosition, Projectile other) {
        this(currentPosition, targetPosition, other.damage, other.health, other.timeCreated, other.fuse, other.speed);
    }

    public abstract void onImpact(Drawable other); // what the projectile does when it hits something

    public abstract void draw(Graphics g);

    public void move() {
        position.add(velocity);
    }

    public void setTarget(Vector targetPosition) {
        this.targetPosition = targetPosition;
        setVelocityFromTarget();
    }

    private void setVelocityFromTarget() {
        Vector displacement = new Vector(targetPosition);
        displacement.add(Vector.scalarMult(position, -1));
        double angle = displacement.getAngle();
        this.velocity = new Vector(speed * Math.cos(angle), speed * Math.sin(angle));
    }

    protected void setDamage(int damage) {
        this.damage = damage;
    }

    protected void setHealth(int health) {
        this.health = health;
    }

    protected void setTimeCreated(int timeCreated) {
        this.timeCreated = timeCreated;
    }

    protected void setFuse(int fuse) {
        this.fuse = fuse;
    }

    protected void setSpeed(int speed) {
        this.speed = speed;
    }

    protected void setReload(int reload) {
        this.reload = reload;
    }

    public int getReload() {
        return reload;
    }

    public int getDamage() {
        return damage;
    }

    public Vector getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }

    public Vector getVelocity() {
        return velocity;
    }
}
