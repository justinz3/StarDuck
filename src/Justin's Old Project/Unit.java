/*
 * Author: Justin Zhu
 * 
 * Description: Final Project class, It's a unit
 * 
 * Created: 6-18-18
 */

import java.util.*;
import java.awt.*;

public class Unit
{
  private Vector velocity;
  private Weapon weapon;
  private Circle unit;
  private double health;
  private int team;
  private double targetX, targetY;
  private double lastAttack; // time of the last time it fired
  private static final double SPEED = 1;
  private static Color[] colors = {Color.blue, Color.red, Color.green, Color.yellow}; // takes in team, gives color
  
  // constructors -----------------------------------------------------------------------------------------------------
  
  public Unit(double x, double y, int team, int weaponType)
  {
    velocity = new Vector(); // no movement
    health = 100;
    unit = new Circle(x, y, velocity, colors[team], 25); // the reference is passed so the velocities should be the same
    targetX = x;
    targetY = y;
    lastAttack = 0;
    this.team = team;
    weapon = new Weapon(weaponType);
  }
  
  // accessors -----------------------------------------------------------------------------------------------------
  
  public Vector getVelocity()
  {
    return velocity;
  }
  
  public double getRange()
  {
    return weapon.getRange();
  }
  
  public void draw(Graphics g)
  {
    Color oldColor = g.getColor();
    
    unit.fill(g);
    g.setColor(Color.white);
    g.drawString(String.format("%.2f", health), (int) unit.getCenterX(), (int) unit.getCenterY());
    
    g.setColor(oldColor);
  }
  
  public void drawRange(Graphics g)
  {
    Circle maxRange = new Circle(unit.getCenterX(), unit.getCenterY(), new Vector(), colors[team], weapon.getRange());
    maxRange.draw(g);
  }
  
  public int getTeam()
  {
    return team;
  }
  
  public Circle getCircle()
  {
    return unit;
  }
  
  public double getHealth()
  {
    return health;
  }
  
  public double getTargetX()
  {
    return targetX;
  }
  
  public double getTargetY()
  {
    return targetY;
  }
  
  public double getLastAttack()
  {
    return lastAttack;
  }
  
  public Weapon getWeapon()
  {
    return weapon;
  }
  
  public boolean inRange(Unit other)
  {
    double dist = Math.hypot(unit.getCenterX() - other.unit.getCenterX(), 
                             unit.getCenterY() - other.unit.getCenterY());
    
    return dist - unit.getRadius() <= weapon.getRange();
  }
  
  // mutators -----------------------------------------------------------------------------------------------------
  
  public void setVelocity(Vector velocity)
  {
    this.velocity = velocity;
    unit.setVelocity(velocity); // just to be sure
  }
  
  public void setRange(double range)
  {
    weapon.setRange(range);
  }
  
  public void setTeam(int team)
  {
    this.team = team;
    unit.setColor(colors[team]);
  }
  
  public void setCircle(Circle unit)
  {
    this.unit = unit;
  }
  
  public void setHealth(double health)
  {
    this.health = health;
  }
  
  public void setTargetX(double targetX)
  {
    this.targetX = targetX;
  }
  
  public void setTargetY(double targetY)
  {
    this.targetY = targetY;
  }
  
  public void setTarget(double targetX, double targetY)
  {
    setTargetX(targetX);
    setTargetY(targetY);
  }
  
  public void setLastAttack(double time)
  {
    this.lastAttack = time;
  }
  
  public void setWeapon(Weapon weapon)
  {
    this.weapon = weapon;
  }
  
  public Projectile fireAt(Unit other)
  {
    // make a new projectile going in the direction of the other Unit
    
    Projectile projectile = new Projectile(unit.getCenterX(), unit.getCenterY(), team, 
                                       new Vector(other.unit.getCenterX() - unit.getCenterX(), other.unit.getCenterY() - unit.getCenterY()), weapon);
    return projectile;
  }
  
  public static void delete(Unit other)
  {
    other = null;
  }
  
  public void move()
  {
    if(!(Math.abs(unit.getCenterX() - targetX) < 1 && Math.abs(unit.getCenterY() - targetY) < 1)) // not at destination
    {
      Vector movement = new Vector(targetX - unit.getCenterX(), targetY - unit.getCenterY());
      movement.multiplyScalar(Math.min(SPEED, movement.getMagnitude()) / movement.getMagnitude());
      setVelocity(movement);
    }
    else
    {
      setVelocity(new Vector());
    }
    
    unit.move();
  }
  
  public static void setGroupTarget(ArrayList<Unit> group, double targetX, double targetY)
  {
    double averageX = 0, averageY = 0;
    
    for(int i = 0; i < group.size(); i++)
    {
      averageX += group.get(i).getCircle().getCenterX();
      averageY += group.get(i).getCircle().getCenterY();
    }
    
    averageX /= group.size();
    averageY /= group.size();
    
    double moveX = targetX - averageX;
    double moveY = targetY - averageY;
    
    for(int i = 0; i < group.size(); i++)
    {
      group.get(i).setTarget(group.get(i).getCircle().getCenterX() + moveX, 
                             group.get(i).getCircle().getCenterY() + moveY);
    }
  }
  
}
