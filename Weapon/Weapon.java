// Description: Each ship will have a/some weapon(s) which will create Projectiles
// Created: 3/2/19

package Weapon;

import Panels.GamePanel;
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
        if(projectile instanceof Bomb) {
            return new Bomb(position, target, team);
        }
        return null; //TODO
    }

    public Projectile fire(Vector position, double radians) {
        Vector target = new Vector(position);
        double fuse = projectile.getFuse();
        target.add(new Vector(projectile.getSpeed() * (fuse > 0 ? fuse / GamePanel.refreshPeriod : 1), Vector.unitVector(radians)));
        return fire(position, target, team);
    }
}
