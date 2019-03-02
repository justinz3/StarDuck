/*
 * Author: Justin Zhu
 * 
 * Description: Classwork for Java Programming during 5-31-18, It's a shape
 * 
 * Created: 5-31-18
 */

import java.util.*;
import java.awt.*;

public abstract class Shape
{
  
  private Vector velocity;
  private double centerX, centerY;
  private Color color;
  
  
  // constructors -----------------------------------------------------------------------------------------------------
  
  public Shape()
  {
    velocity = new Vector();
  }
  
  public Shape(double x, double y, Vector velocity, Color color)
  {
    this.velocity = velocity;
    centerX = x;
    centerY = y;
    this.color = color;
  }
  
  public Shape(Shape other)
  {
    this(other.centerX, other.centerY, other.velocity, other.color);
  }
  
  // accessors -----------------------------------------------------------------------------------------------------
  
  public Vector getVelocity()
  {
    return velocity;
  }
  
  public double getCenterX()
  {
    return centerX;
  }
  
  public double getCenterY()
  {
    return centerY;
  }
  
  public Color getColor()
  {
    return color;
  }
  
  public abstract void draw(Graphics g);
  
  public abstract void fill(Graphics g);
  
  public abstract boolean containsPoint(double x, double y);
  
  public abstract int minX();
  
  public abstract int maxX();
  
  public abstract int minY();
  
  public abstract int maxY();
  
  public abstract double getArea();
  
  
  // mutators -----------------------------------------------------------------------------------------------------
  
  public void setVelocity(Vector velocity)
  {
    this.velocity = velocity;
  }
  
  public void setCenterX(double x)
  {
    centerX = x;
  }
  
  public void setCenterY(double y)
  {
    centerY = y;
  }
  
  public void setColor(Color color)
  {
    this.color = color;
  }
  
  public void move(){
    setCenterX(getCenterX() + getVelocity().getX());
    setCenterY(getCenterY() + getVelocity().getY());
  }
  
  public static void swapMomentum(Shape a, Shape b)
  {
    double areaA = a.getArea();
    double areaB = b.getArea();
    
    Vector velocityA = a.getVelocity();
    Vector velocityB = b.getVelocity();
    
//    System.out.println("START");
//    System.out.println(velocityA);
//    System.out.println(velocityB);
//    System.out.println();
    
    // calculate momentum
    velocityA.setX(velocityA.getX() * areaA);
    velocityA.setY(velocityA.getY() * areaA);
    velocityB.setX(velocityB.getX() * areaB);
    velocityB.setY(velocityB.getY() * areaB);
    
//    System.out.println(velocityA);
//    System.out.println(velocityB);
//    System.out.println();
    
    // swap
    double tempX, tempY;
    tempX = velocityA.getX();
    velocityA.setX(velocityB.getX());
    velocityB.setX(tempX);
    tempY = velocityA.getY();
    velocityA.setY(velocityB.getY());
    velocityB.setY(tempY);
    
//    System.out.println(velocityA);
//    System.out.println(velocityB);
//    System.out.println();
    
    // divide to obtain speeds
    velocityA.setX(velocityA.getX() / areaA);
    velocityA.setY(velocityA.getY() / areaA);
    velocityB.setX(velocityB.getX() / areaB);
    velocityB.setY(velocityB.getY() / areaB);
    
//    System.out.println(velocityA);
//    System.out.println(velocityB);
//    System.out.println("END");
    
    // set back to the original
    a.setVelocity(velocityA);
    b.setVelocity(velocityB);
  }
}
