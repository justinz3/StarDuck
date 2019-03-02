/*
 * Author: Justin Zhu
 * 
 * Description: Classwork for Java Programming during 5-31-18, edited 6-18-18, It's a vector
 * 
 * Created: 5-31-18
 */

import java.util.*;
import java.awt.*;

public class Vector
{
  
  private double x, y;
  
  
  // constructors -----------------------------------------------------------------------------------------------------
  
  public Vector()
  {
    x = y = 0;
  }
  
  public Vector(double x, double y)
  {
    this();
    setX(x);
    setY(y);
  }
  
  public Vector(Vector other)
  {
    this(other.getX(), other.getY());
  }
  
  
  // accessors -----------------------------------------------------------------------------------------------------
  
  public double getX()
  {
    return x;
  }
  
  public double getY()
  {
    return y;
  }
  
  public String toString()
  {
    return String.format("<%.2f, %.2f>", x, y);
  }
  
  public Vector add(Vector a, Vector b)
  {
    return new Vector(a.getX() + b.getX(), a.getY() + b.getY());
  }
  
  public double getMagnitude()
  {
    return Math.hypot(x, y);
  }
  
  public boolean isMoving()
  {
    return !(x == 0 && y == 0);
  }
  
  public static double dot(Vector a, Vector b)
  {
    return a.getX() * b.getX() + a.getY() * b.getY();
  }
  
  public static Vector scalarMult(Vector a, double scalar)
  {
    return new Vector(a.getX() * scalar, a.getY() * scalar);
  }
  
  
  // mutators -----------------------------------------------------------------------------------------------------
  
  public void setX(double x)
  {
    this.x = x;
  }
  
  public void setY(double y)
  {
    this.y = y;
  }
  
  public void multiplyScalar(double scalar)
  {
    x *= scalar;
    y *= scalar;
  }
  
}
