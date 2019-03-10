package Physics;

import Player.Ship;

public abstract class Hittable {

    private double health;
    private Hitbox hitbox;
    private double damage; // damage done on impact

    public Hittable() {
        this(null, 0, 0);
    }

    public Hittable(Hitbox hitbox, double health, double damage) {
        this.hitbox = hitbox;
        this.health = health;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    protected void setHitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public double getHealth() {
        return health;
    }

    protected void setHealth(double health) {
        this.health = health;
    }

    protected void setDamage(int damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    public boolean impact(Hittable other) {
        return other.takeDamage(damage);
    }

    public boolean takeDamage(double damage) {
        health -= damage;
        return health > 0;
    }
}
