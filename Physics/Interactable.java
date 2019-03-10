package Physics;

import java.awt.*;

public abstract class Interactable extends Hittable implements Drawable, Movable {

    public Interactable(Hitbox hitbox, double health, double damage) {
        super(hitbox, health, damage);
    }

    public abstract void move();

    public abstract void draw(Graphics g);
}
