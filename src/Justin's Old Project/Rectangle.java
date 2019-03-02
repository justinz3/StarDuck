/*
 * Author: Justin Zhu
 * 
 * Description: Classwork for Java Programming during 6-21-18, It's a rectangle
 * 
 * Created: 6-21-18
 */

import java.util.*;
import java.awt.*;

public class Rectangle extends Shape
{
  private double width, height;
  
  // constructors -----------------------------------------------------------------------------------------------------
  public Rectangle()
  {
    this(100, 100, new Vector(), Color.black, 100, 100);
  }
  
  public Rectangle(double x, double y, Vector velocity, Color color, double width, double height)
  {
    setVelocity(velocity);
    setCenterX(x);
    setCenterY(y);
    setColor(color);
    setWidth(width);
    setHeight(height);
  }
  
  public Rectangle(Rectangle other)
  {
    this(other.getCenterX(), other.getCenterY(), other.getVelocity(), 
         other.getColor(), other.getWidth(), other.getHeight());
  }
  
  public Rectangle(Circle other)
  {
    this(other.getCenterX(), other.getCenterY(), other.getVelocity(), 
         other.getColor(), other.getRadius() * 2, other.getRadius() * 2);
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
  
  public void draw(Graphics g)
  {
    Color oldColor = g.getColor();
    g.setColor(getColor());
    // Translates circle's center to rectangle's origin for drawing.
    g.drawRect((int) (getCenterX() - Math.max(-width / 2, width / 2)), 
               (int) (getCenterY() - Math.max(-height / 2, height / 2)), 
               (int) Math.abs(width), (int) Math.abs(height));
    g.setColor(oldColor);
  }
  
  public void fill(Graphics g)
  {
    Color oldColor = g.getColor();
    g.setColor(getColor());
    // Translates circle's center to rectangle's origin for drawing.
    g.fillRect((int) (getCenterX() - Math.max(-width / 2, width / 2)), 
               (int) (getCenterY() - Math.max(-height / 2, height / 2)), 
               (int) Math.abs(width), (int) Math.abs(height));
    g.setColor(oldColor);
  }
  
  public boolean containsPoint(double x, double y){
    
    return (minX() <= x && x <= maxX()) && (minY() <= y && y <= maxY());
  }
  
  public boolean isTouching(Rectangle other)
  {
    return ((minX() <= other.minX() && other.minX() <= maxX()) || (other.minX() <= minX() && minX() <= other.maxX())) && 
           ((minY() <= other.minY() && other.minY() <= maxY()) || (other.minY() <= minY() && minY() <= other.maxY()));
  }
  
  public boolean willTouch(Rectangle other)
  {
    Rectangle a = new Rectangle(this), b = new Rectangle(other);
    
    a.move();
    b.move();
    return a.isTouching(b);
  }
  
  public int minX()
  {
    return (int) (getCenterX() - width / 2);
  }
  
  public int maxX()
  {
    return (int) (getCenterX() + width / 2);
  }
  
  public int minY()
  {
    return (int) (getCenterY() - height / 2);
  }
  
  public int maxY()
  {
    return (int) (getCenterY() + height / 2);
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
  
  public void setLeftX(double x) // sets the center
  {
    this.setCenterX(getCenterX() + x - (getCenterX() - width / 2));
  }
  
  public void stretchToX(double x) // extends the width
  {
    this.setWidth(x - (this.getCenterX() -  width / 2));
  }
  
  public void setTopY(double y) // sets the center
  {
    this.setCenterY(getCenterY() + y - (getCenterY() - height / 2));
  }
  
  public void stretchToY(double y) // extends the height
  {
    this.setHeight(y - (this.getCenterY() - height / 2));
  }
}
