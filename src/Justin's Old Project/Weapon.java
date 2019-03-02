/*
 * Author: Justin Zhu
 * 
 * Description: Final Project class, It's a weapon, projectiles get their information from Weapon
 * 
 * Created: 6-18-18
 */

import java.util.*;
import java.awt.*;

public class Weapon
{
  private double damage, length, speed, range, reload;
  private static double[] damages = {20,    150,    5,       5,     50 };   // 100 health per unit
  private static double[] lengths = {10,    20,     5,       50,    10 };   // in pixels
  private static double[] speeds =  {5,     5,      5,       10,    10 };   // pixels / 5 milliseconds
  private static double[] ranges =  {250,   400,    200,     450,   300};   // in pixels
  private static double[] reloads = {1000,  7000,   150,     100,   500};   // in milliseconds
  //                                 Rifle, Sniper, Machine, Laser, Super
  
  // constructors -----------------------------------------------------------------------------------------------------
  
  public Weapon(int type)
  {
    damage = damages[type];
    length = lengths[type];
    speed = speeds[type];
    range = ranges[type];
    reload = reloads[type];
  }
  
  // accessors -----------------------------------------------------------------------------------------------------
  
  public double getDamage()
  {
    return damage;
  }
  
  public double getLength()
  {
    return length;
  }
  
  public double getSpeed()
  {
    return speed;
  }
  
  public double getRange()
  {
    return range;
  }
  
  public double getReload()
  {
    return reload;
  }
  
  // mutators -----------------------------------------------------------------------------------------------------
  
  public void setDamage(double damage)
  {
    this.damage = damage;
  }
  
  public void setLength(double length)
  {
    this.length = length;
  }
  
  public void setSpeed(double speed)
  {
    this.speed = speed;
  }
  
  public void setRange(double range)
  {
    this.range = range;
  }
  
  public void setReload(double reload)
  {
    this.reload = reload;
  }
}
