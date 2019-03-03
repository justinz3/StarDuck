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

    public abstract boolean isInside(Vector position);

    // Getters and Setters
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

}
