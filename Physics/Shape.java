// Description: Superclass for all Shapes which will compose the Hitboxes
// Created: 3/2/19

package Physics;

import javax.swing.*;
import java.awt.*;

public abstract class Shape implements Drawable {

    private Vector position;

    public Shape() {
        position = new Vector(0, 0);
    }

    public Shape(Vector position) {
        this.position = position;
    }

    public abstract boolean isTouching(Shape other);

    public abstract void draw(Graphics g, JPanel panel);

    public abstract boolean containsPoint(Vector position);

    public void move() {
        // do nothing, shapes don't move? or we can apply a velocity, so that the hitbox rectangles will have the same as the ships
    }

    // Getters and Setters
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

}
