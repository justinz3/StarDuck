// Description: Each ship will have a/some weapon(s) which will create Projectiles
// Created: 3/2/19

package Weapon;

import Physics.*;

public class Weapon {

    private Projectile projectile;

    public Weapon(Projectile projectile) {
        this.projectile = projectile;
    }

    public Projectile fire(Vector position, Vector target) {
        if(projectile instanceof Laser) {
            return new Laser(new Vector(position), new Vector(target));
        }
        return null; //TODO
    }

    public Projectile fire(Vector position, double radians) {
        Vector target = new Vector(position);
        target.add(new Vector(projectile.getSpeed(), Vector.unitVector(radians)));
        System.out.println(position + " " + target);
        return fire(position, target);
    }
}
