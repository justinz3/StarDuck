// Description: Each ship will have a/some weapon(s) which will create Projectiles
// Created: 3/2/19

package Weapon;

import Physics.*;

public class Weapon {

    private Projectile projectile;
    private int team;

    public Weapon(Projectile projectile, int team) {
        this.projectile = projectile;
        this.team = team;
    }

    public Projectile fire(Vector position, Vector target, int team) {
        if(projectile instanceof Laser) {
            return new Laser(new Vector(position), new Vector(target), team);
        }
        return null; //TODO
    }

    public Projectile fire(Vector position, double radians) {
        Vector target = new Vector(position);
        target.add(new Vector(projectile.getSpeed(), Vector.unitVector(radians)));
        return fire(position, target, team);
    }
}
