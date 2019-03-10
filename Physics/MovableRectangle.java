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

public class MovableRectangle extends MovableShape {
    private double width, height;

    // constructors -----------------------------------------------------------------------------------------------------
//    public MovableRectangle() {
//        this(new Physics.Vector(100, 100), 50, 50);
//    }

    public MovableRectangle(Vector position, double width, double height) {
        super(position);
        setWidth(width);
        setHeight(height);
    }

    public MovableRectangle(MovableRectangle other) {
        this(other.getPosition(), other.getWidth(), other.getHeight());
    }


    // accessors -----------------------------------------------------------------------------------------------------

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDiagonal() {
        return Math.hypot(width, height);
    }

    public void draw(Graphics g) {
        // Translates circle's center to rectangle's origin for drawing.

        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(getRotatedShape(Math.toRadians(getRotation())));
    }

    public void fill(Graphics g) {
        // Translates circle's center to rectangle's origin for drawing.
        g.fillRect((int) (getPosition().getX()),
                (int) (getPosition().getY()),
                (int) Math.abs(width), (int) Math.abs(height));
    }

    public boolean containsPoint(double x, double y) {
        return containsPoint(new Vector(x, y));
    }

    public boolean containsPoint(Vector point) {

        double x = point.getX(), y = point.getY();

        if(getRotation() % 180 == 0)
            return (minX() <= x && x <= maxX()) && (minY() <= y && y <= maxY());
        if(getRotation() % 90 == 0) // This has not been tested, may result in bugs
            return (minY() <= x && x <= maxY()) && (minX() <= y && y <= maxX());

        ArrayList<Vector> points = toPoints();
        Vector top, left, right, bottom;
        top = left = right = bottom = points.get(0);
        for(int i = 1; i < points.size(); i++) {
            if(top.getY() > points.get(i).getY())
                top = points.get(i);
            if(bottom.getY() < points.get(i).getY())
                bottom = points.get(i);
            if(left.getX() > points.get(i).getX())
                left = points.get(i);
            if(right.getX() < points.get(i).getX())
                right = points.get(i);
        }

        // we do aboveLine for the top (and belowLine for the bottom) because the y-axis of graphics is inverted,
        //          so the top has lower coordinates than the bottom
        return (new Line(top, left)).aboveLine(point) && (new Line(top, right)).aboveLine(point) &&
                (new Line(bottom, left)).belowLine(point) && (new Line(bottom, right)).belowLine(point);
    }

    public boolean isTouching(MovableShape other) {
        if (other instanceof MovableRectangle) {
            for(Vector point : ((MovableRectangle) other).toPoints()) {
                if(containsPoint(point)) {
                    return true;
                }
            }
            return false;
//            return ((minX() <= rect.minX() && rect.minX() <= maxX()) || (rect.minX() <= minX() && minX() <= rect.maxX())) &&
//                    ((minY() <= rect.minY() && rect.minY() <= maxY()) || (rect.minY() <= minY() && minY() <= rect.maxY()));
        }

        // TODO do other shapes
        return false;
    }

    public boolean willTouch(MovableRectangle other) {
        MovableRectangle a = new MovableRectangle(this), b = new MovableRectangle(other);
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

        ArrayList<Vector> points = toPoints();

        for(int i = 0; i < points.size(); i++) {
            lines.add(new Line(points.get(i), points.get((i + 1) % points.size())));
        }

        return lines;
    }

    public ArrayList<Vector> toPoints() {
        ArrayList<Vector> points = new ArrayList<>();

        double diagonal = getDiagonal() / 2;

        // ______
        // |\ e/|
        // | \/f|
        // |f/\ |
        // |/ e\|
        // ______

        double e, f; // angles
        e = 2 * Math.asin(width / 2 / diagonal);
        f = 2 * Math.asin(height / 2 / diagonal);

        Rectangle position = getRotatedShape(Math.toRadians(getRotation())).getBounds();
        Vector center = new Vector(position.getCenterX(), position.getCenterY());
        Vector a, b, c, d; // corners
        a = Vector.add(new Vector(diagonal, Vector.unitVector(Math.toRadians(getRotation()) + f / 2)), center);
        b = Vector.add(new Vector(diagonal, Vector.unitVector(Math.toRadians(getRotation()) + f / 2 + e)), center);
        c = Vector.add(new Vector(diagonal, Vector.unitVector(Math.toRadians(getRotation() + 180) + f / 2)), center);
        d = Vector.add(new Vector(diagonal, Vector.unitVector(Math.toRadians(getRotation() + 180) + e + f / 2)), center);

        points.add(a);
        points.add(b);
        points.add(c);
        points.add(d);

        return points;
    }

    public Shape getRotatedShape(double radians) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(radians, getCenterOfRotation().getX(), getCenterOfRotation().getY());

        int x = (int) (getPosition().getX() - Math.max(-width / 2, width / 2));
        int y = (int) (getPosition().getY() - Math.max(-height / 2, height / 2));
        Rectangle rect = new Rectangle(x, y, (int) width, (int) height);
        Shape newShape = tx.createTransformedShape(rect);
        return newShape;
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
