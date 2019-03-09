// Description: Superclass for all Shapes which will compose the Hitboxes
// Created: 3/2/19

package Physics;

import java.awt.*;

public abstract class MoveableShape implements Drawable, Moveable {

    private Vector position;
    private int rotation = 0;
    private Vector centerOfRotation;

    public MoveableShape() {
        position = new Vector(0, 0);
    }

    public MoveableShape(Vector position) {
        this.position = position;
        this.centerOfRotation = new Vector(position);
    }

    public abstract boolean isTouching(MoveableShape other);

    public abstract void draw(Graphics g);

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

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int degrees) {
        this.rotation = degrees;
    }

    public Vector getCenterOfRotation() {
        return centerOfRotation;
    }

    public void setCenterOfRotation(Vector centerOfRotation) {
        this.centerOfRotation = centerOfRotation;
    }

}
