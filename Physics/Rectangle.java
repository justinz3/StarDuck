package Physics;

/*
 * Author: Justin Zhu
 *
 * Description: Classwork for Java Programming during 6-21-18, It's a rectangle
 *
 * Created: 6-21-18
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Rectangle extends Shape
{
    private double width, height;
    private Color color;

    // constructors -----------------------------------------------------------------------------------------------------
    public Rectangle() {
        this(new Physics.Vector(100, 100), Color.red, 50, 50);
    }

    public Rectangle(Vector position, Color color, double width, double height)
    {
        super(position);
        setColor(color);
        setWidth(width);
        setHeight(height);
    }

    public Rectangle(Rectangle other)
    {
        this(other.getPosition(), other.getColor(), other.getWidth(), other.getHeight());
    }


    // accessors -----------------------------------------------------------------------------------------------------

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics g, JPanel panel)
    {
        Color oldColor = g.getColor();
        g.setColor(getColor());
        // Translates circle's center to rectangle's origin for drawing.
        g.drawRect((int) (getPosition().getX() - Math.max(-width / 2, width / 2)),
                (int) (getPosition().getY() - Math.max(-height / 2, height / 2)),
                (int) Math.abs(width), (int) Math.abs(height));
        g.setColor(oldColor);
    }

    public void fill(Graphics g)
    {
        Color oldColor = g.getColor();
        g.setColor(getColor());
        // Translates circle's center to rectangle's origin for drawing.
        g.fillRect((int) (getPosition().getX() - Math.max(-width / 2, width / 2)),
                (int) (getPosition().getY() - Math.max(-height / 2, height / 2)),
                (int) Math.abs(width), (int) Math.abs(height));
        g.setColor(oldColor);
    }

    public boolean containsPoint(double x, double y){

        return (minX() <= x && x <= maxX()) && (minY() <= y && y <= maxY());
    }

    public boolean isTouching(Shape other)
    {
        if(other instanceof Rectangle) {
            Rectangle rect = (Rectangle) other;
            return ((minX() <= rect.minX() && rect.minX() <= maxX()) || (rect.minX() <= minX() && minX() <= rect.maxX())) &&
                    ((minY() <= rect.minY() && rect.minY() <= maxY()) || (rect.minY() <= minY() && minY() <= rect.maxY()));
        }

        return false;
    }

    public boolean isInside(Vector position) {
        return Math.abs(position.getX() - getPosition().getX()) <= width / 2
                && Math.abs(position.getY() - getPosition().getY()) <= height / 2;
    }

    public boolean willTouch(Rectangle other)
    {
        Rectangle a = new Rectangle(this), b = new Rectangle(other);
        return a.isTouching(b);
    }

    public int minX()
    {
        return (int) (getPosition().getX() - width / 2);
    }

    public int maxX()
    {
        return (int) (getPosition().getX() + width / 2);
    }

    public int minY()
    {
        return (int) (getPosition().getY() - height / 2);
    }

    public int maxY()
    {
        return (int) (getPosition().getY() + height / 2);
    }

    public double getArea()
    {
        return width * height;
    }

    public String toString()
    {
        return String.format("TL: (%d, %d), W: %d, H: %d", (int) minX(), (int) minY(),
                (int) getWidth(), (int) getHeight());
    }

    // mutators -----------------------------------------------------------------------------------------------------

    public void setWidth(double width)
    {
        this.width = width;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLeftX(double x) // sets the center
    {
        setPosition(new Vector((getPosition().getX() + x - (getPosition().getX() - width / 2)), getPosition().getY()));
    }

    public void stretchToX(double x) // extends the width
    {
        this.setWidth(x - (getPosition().getX() -  width / 2));
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
