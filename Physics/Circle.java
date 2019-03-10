/*
 * Author: Justin Zhu
 *
 * Description: Classwork for Java Programming during 5-31-18, edited 6-18-18, It's a circle
 *
 * Created: 5-31-18
 */

package Physics;

import java.awt.*;

public class Circle extends MovableShape
{
    private double radius;
    private Color color;

    // constructors -----------------------------------------------------------------------------------------------------

    public Circle() {
        this(new Vector(100, 100), Color.red, 50);
    }

    public Circle(Vector position, Color color, double radius)
    {
        super(position);
        setColor(color);
        setRadius(radius);
    }

    public Circle(Circle other)
    {
        this(other.getPosition(), other.getColor(), other.getRadius());
    }



    // accessors -----------------------------------------------------------------------------------------------------

    public double getRadius()
    {
        return radius;
    }

    public void draw(Graphics g)
    {
        Color oldColor = g.getColor();
        g.setColor(getColor());
        // Translates circle's center to rectangle's origin for drawing.
        g.drawOval((int) (getPosition().getX() - radius), (int) (getPosition().getY() - radius), (int) (radius * 2), (int) (radius * 2));
        g.setColor(oldColor);
    }

    public void fill(Graphics g)
    {
        Color oldColor = g.getColor();
        g.setColor(getColor());
        // Translates circle's center to rectangle's origin for drawing.
        g.fillOval((int) (getPosition().getX() - radius), (int) (getPosition().getY() - radius), (int) (radius * 2), (int) (radius * 2));
        g.setColor(oldColor);
    }

    public boolean containsPoint(double x, double y){
        int xSquared = (int) ((x - getPosition().getX()) * (x - getPosition().getX()));
        int ySquared = (int) ((y - getPosition().getY()) * (y - getPosition().getY()));
        int radiusSquared = (int) (radius * radius);
        return xSquared + ySquared - radiusSquared <= 0;
    }

    public boolean isTouching(MovableShape other)
    {
        if(other instanceof Circle) {
            Circle circle = (Circle) other;
            return (int) Math.hypot(getPosition().getX() - circle.getPosition().getX(), getPosition().getY() - circle.getPosition().getY())
                    <= getRadius() + circle.getRadius();
        }
        return false;
    }

    public boolean containsPoint(Vector position) {
        return containsPoint(position.getX(), position.getY());
    }

    public int minX()
    {
        return (int) (getPosition().getX() - radius);
    }

    public int maxX()
    {
        return (int) (getPosition().getX() + radius);
    }

    public int minY()
    {
        return (int) (getPosition().getY() - radius);
    }

    public int maxY()
    {
        return (int) (getPosition().getY() + radius);
    }

    public double getArea()
    {
        return Math.PI * radius * radius;
    }

    public Color getColor() {
        return color;
    }

    // mutators -----------------------------------------------------------------------------------------------------

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

