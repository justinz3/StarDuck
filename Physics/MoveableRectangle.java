package Physics;

/*
 * Author: Justin Zhu
 *
 * Description: Classwork for Java Programming during 6-21-18, It's a rectangle
 *
 * Created: 6-21-18
 */

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;

public class MoveableRectangle extends MoveableShape {
    private double width, height;

    // constructors -----------------------------------------------------------------------------------------------------
//    public MoveableRectangle() {
//        this(new Physics.Vector(100, 100), 50, 50);
//    }

    public MoveableRectangle(Vector position, double width, double height) {
        super(position);
        setWidth(width);
        setHeight(height);
    }

    public MoveableRectangle(MoveableRectangle other) {
        this(other.getPosition(), other.getWidth(), other.getHeight());
    }


    // accessors -----------------------------------------------------------------------------------------------------

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void draw(Graphics g) {
        // Translates circle's center to rectangle's origin for drawing.
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(getRotation()), getCenterOfRotation().getX(), getCenterOfRotation().getY());

        int x = (int) (getPosition().getX() - Math.max(-width / 2, width / 2));
        int y = (int) (getPosition().getY() - Math.max(-height / 2, height / 2));
        Rectangle rect = new Rectangle(x, y, (int) width, (int) height);
        Shape newShape = tx.createTransformedShape(rect);
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(newShape);

        //g.drawRect(x, y, (int) Math.abs(width), (int) Math.abs(height));
    }

    public void fill(Graphics g) {
        // Translates circle's center to rectangle's origin for drawing.
        g.fillRect((int) (getPosition().getX()),
                (int) (getPosition().getY()),
                (int) Math.abs(width), (int) Math.abs(height));
    }

    public boolean containsPoint(double x, double y) {

        return (minX() <= x && x <= maxX()) && (minY() <= y && y <= maxY());
    }

    public boolean containsPoint(Vector position) {
        return containsPoint(position.getX(), position.getY());
    }

    public boolean isTouching(MoveableShape other) {
        if (other instanceof MoveableRectangle) {
            MoveableRectangle rect = (MoveableRectangle) other;
            return ((minX() <= rect.minX() && rect.minX() <= maxX()) || (rect.minX() <= minX() && minX() <= rect.maxX())) &&
                    ((minY() <= rect.minY() && rect.minY() <= maxY()) || (rect.minY() <= minY() && minY() <= rect.maxY()));
        }

        // TODO do other shapes
        return false;
    }

    public boolean willTouch(MoveableRectangle other) {
        MoveableRectangle a = new MoveableRectangle(this), b = new MoveableRectangle(other);
        return a.isTouching(b);
    }

    public int minX() {
        return (int) (getPosition().getX());
    }

    public int maxX() {
        return (int) (getPosition().getX() + width);
    }

    public int minY() {
        return (int) (getPosition().getY());
    }

    public int maxY() {
        return (int) (getPosition().getY() + height);
    }

    public double getArea() {
        return width * height;
    }

    public String toString() {
        return String.format("TL: (%d, %d), W: %d, H: %d", (int) minX(), (int) minY(),
                (int) getWidth(), (int) getHeight());
    }

    public ArrayList<Line> toLines() {
        ArrayList<Line> lines = new ArrayList<>();

        //lines.add(new Line(new Vector()));

        return null; // TODO finish
    }

    // mutators -----------------------------------------------------------------------------------------------------

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setLeftX(double x) // sets the center
    {
        setPosition(new Vector((getPosition().getX() + x - (getPosition().getX() - width / 2)), getPosition().getY()));
    }

    public void stretchToX(double x) // extends the width
    {
        this.setWidth(x - (getPosition().getX() - width / 2));
    }

    public void setTopY(double y) // sets the center
    {
        setPosition(new Vector(getPosition().getX(), (getPosition().getY() + y - (getPosition().getY() - height / 2))));
    }

    public void stretchToY(double y) // extends the height
    {
        this.setHeight(y - (getPosition().getY() - height / 2));
    }
}
