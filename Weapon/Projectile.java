// Description: A Projectile that will move and inflict damage
// Created: 3/2/19

package Weapon;

import Physics.*;

import java.awt.*;

public abstract class Projectile extends Interactable {

    private int timeCreated;
    private int fuse;
    private Vector targetPosition;
    private Vector position, velocity;
    private double speed;
    private int reload;

    public Projectile(Vector currentPosition, Vector targetPosition, double damage, double health, int timeCreated, int fuse, double speed, Hitbox hitbox, int team) {
        super(hitbox, 1, damage, team); // health should be 1, so that it dies upon impact
        this.timeCreated = timeCreated;
        this.fuse = fuse;
        this.speed = speed;

        this.position = currentPosition;
        this.targetPosition = targetPosition;
        setVelocityFromTarget();
    }

    public Projectile(Vector currentPosition, Vector targetPosition, Projectile other, int team) {
        this(currentPosition, targetPosition, other.getDamage(), other.getHealth(), other.timeCreated, other.fuse, other.speed, other.getHitbox(), team);
    }

    public abstract void onImpact(Hittable other); // what the projectile does when it hits something

    public abstract void draw(Graphics g);

    public abstract Dimension getImageSize();

    public void move() {
        position.add(velocity);
        getHitbox().get(0).setPosition(position);
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
