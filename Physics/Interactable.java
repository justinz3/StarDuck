package Physics;

import java.awt.*;

public abstract class Interactable extends Hittable implements Drawable, Movable {

    public Interactable(Hitbox hitbox, double health, double damage, int team) {
        super(hitbox, health, damage, team);
    }

    public abstract void move();

    public abstract void draw(Graphics g);
}
