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
            System.out.printf("Position: %s, Target %s\n", position, target);
            return new Laser(position, target);
        }
        return null; //TODO
    }
}
