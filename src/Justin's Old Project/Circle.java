/*
 * Author: Justin Zhu
 * 
 * Description: Classwork for Java Programming during 5-31-18, edited 6-18-18, It's a circle
 * 
 * Created: 5-31-18
 */

import java.util.*;
import java.awt.*;

public class Circle extends Shape
{
  
  private double radius;
  
  // constructors -----------------------------------------------------------------------------------------------------
  public Circle()
  {
    this(100, 100, new Vector(), Color.black, 100);
  }
  
  public Circle(double x, double y, Vector velocity, Color color, double radius)
  {
    setVelocity(velocity);
    setCenterX(x);
    setCenterY(y);
    setColor(color);
    setRadius(radius);
  }
  
  public Circle(Circle other)
  {
    this(other.getCenterX(), other.getCenterY(), other.getVelocity(), other.getColor(), other.getRadius());
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
    g.drawOval((int) (getCenterX() - radius), (int) (getCenterY() - radius), (int) (radius * 2), (int) (radius * 2));
    g.setColor(oldColor);
  }
  
  public void fill(Graphics g)
  {
    Color oldColor = g.getColor();
    g.setColor(getColor());
    // Translates circle's center to rectangle's origin for drawing.
    g.fillOval((int) (getCenterX() - radius), (int) (getCenterY() - radius), (int) (radius * 2), (int) (radius * 2));
    g.setColor(oldColor);
  }
  
  public boolean containsPoint(double x, double y){
    int xSquared = (int) ((x - getCenterX()) * (x - getCenterX()));
    int ySquared = (int) ((y - getCenterY()) * (y - getCenterY()));
    int radiusSquared = (int) (radius * radius);
    return xSquared + ySquared - radiusSquared <= 0;
  }
  
  public boolean isTouching(Circle other)
  {
    return (int) Math.hypot(getCenterX() - other.getCenterX(), getCenterY() - other.getCenterY())
      <= getRadius() + other.getRadius();
  }
  
  public boolean willTouch(Circle other)
  {
    Circle a = new Circle(this), b = new Circle(other);
    
    a.move();
    b.move();
    return a.isTouching(b);
  }
  
  public int minX()
  {
    return (int) (getCenterX() - radius);
  }
  
  public int maxX()
  {
    return (int) (getCenterX() + radius);
  }
  
  public int minY()
  {
    return (int) (getCenterY() - radius);
  }
  
  public int maxY()
  {
    return (int) (getCenterY() + radius);
  }
  
  public double getArea()
  {
    return Math.PI * radius * radius;
  }
  
  // mutators -----------------------------------------------------------------------------------------------------
  
  public void setRadius(double radius)
  {
    this.radius = radius;
  }
  
}
