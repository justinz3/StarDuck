/*
 * Author: Justin Zhu
 * 
 * Description: Final Project class, It's a projectile
 * 
 * Created: 6-18-18
 */

import java.util.*;
import java.awt.*;

public class Projectile
{
  private Vector velocity;
  private double x, y;
  private double length;
  private double speed;
  private double damage;
  private int team;
  private static Color[] colors = {Color.blue, Color.red, Color.yellow, Color.green}; // takes in team, gives color
  
  // constructors -----------------------------------------------------------------------------------------------------
  
  public Projectile(double x, double y, int team, Vector direction, Weapon weapon)
  {
    damage = weapon.getDamage();
    length = weapon.getLength();
    speed = weapon.getSpeed();
    velocity = new Vector(direction); // move along direction, but readjust the speed
    velocity.multiplyScalar(speed / velocity.getMagnitude());
    this.x = x;
    this.y = y;
    this.team = team;
  }
  
  // accessors -----------------------------------------------------------------------------------------------------
  
  public Vector getVelocity()
  {
    return velocity;
  }
  
  public double getX()
  {
    return x;
  }
  
  public double getY()
  {
    return y;
  }
  
  public void draw(Graphics g)
  {
    Color oldColor = g.getColor();
    g.setColor(colors[team]);
    
    double endx, endy; // we need to get the endpoint
    Vector adjusted = new Vector(velocity);
    adjusted.multiplyScalar(length / adjusted.getMagnitude());
    endx = x + adjusted.getX();
    endy = y + adjusted.getY();
    
    g.drawLine((int) x, (int) y, (int) endx, (int) endy);
    g.setColor(oldColor);
  }
  
  public int getTeam()
  {
    return team;
  }
  
  public double getDamage()
  {
    return damage;
  }
  
  public boolean hasHit(Unit other)
  {
//    System.out.println(other.getCircle().containsPoint(x, y));
    return other.getCircle().containsPoint(x, y) && (other.getTeam() != team);
  }
  
  // mutators -----------------------------------------------------------------------------------------------------
  
  public void setVelocity(Vector velocity)
  {
    this.velocity = velocity;
  }
  
  public void setX(double x)
  {
    this.x = x;
  }
  
  public void setY(double y)
  {
    this.y = y;
  }
  
  public void setTeam(int team)
  {
    this.team = team;
  }
  
  public void setDamage(double damage)
  {
    this.damage = damage;
  }
  
  public void move()
  {
    x += velocity.getX();
    y += velocity.getY();
  }
  
  public void hit(Unit other) 
  {
    if(!hasHit(other) || other.getTeam() == team)
      return;
    
    Random generator = new Random();
    
    double factor = generator.nextDouble() / 10; // we want damage to vary by 10%
    factor *= (generator.nextInt(2) == 0) ? -1 : 1; // + or - that %age
    
    double health = other.getHealth();
    double dealt = Math.max(1, damage * (1 + factor));
    // System.out.println(factor + " " + dealt);
    health -= dealt;
    
    other.setHealth(health);
  }
  
  public static void delete(Projectile other)
  {
    other = null;
  }
}
