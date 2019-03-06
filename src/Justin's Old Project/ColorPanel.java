/*
 * Author: Justin Zhu
 * 
 * Description: Used as a reference
 * 
 * Created: 6-18-18
 */

import Physics.Circle;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class ColorPanel extends JPanel{
  
  private ArrayList<Shape> shapes;
  private int selected;               // Used to track unselected shape
  private int x, y;                   // Used to track mouse coordinates  
  private javax.swing.Timer timer;
  private ArrayList<Integer> prevHit; // Keeps track of the item last hit by a shape, -1 is a wall
  private final double FRICTION = 0.00001;
  
  public ColorPanel(Color backColor, int width, int height){
    shapes = new ArrayList<Shape>();
    prevHit = new ArrayList<Integer>();
    setBackground(backColor);
//    setPreferredSize(new Dimension(width, height));
    setSize(width, height);
    selected = -1;
    shapes.add(new Circle());
    shapes.add(new Circle(200, 100, new Vector(), Color.red, 25));
    shapes.add(new Circle(100, 100, new Vector(), Color.blue, 50));
    for(int i = 0; i < shapes.size(); i++)
    {
      prevHit.add(-1);
    }
    
    timer = new javax.swing.Timer(5, new AnimationListener());
    timer.start();
    addMouseListener(new UserListener()); 
    addMouseMotionListener(new PanelMotionListener());
  }
  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    for(int i = 0; i < shapes.size(); i++)
    {
      shapes.get(i).fill(g);
    }
  }
  
  public void stopSelectedMovement()
  {
    if(selected != -1)
    {
      shapes.get(selected).setVelocity(new Vector());
    }
  }
  
  private class AnimationListener implements ActionListener{
    
    public void actionPerformed(ActionEvent e){
      
      for(int i = 0; i < shapes.size(); i++)
      {
        for(int j = 0; j < shapes.size(); j++)
        {
          if(i == j || (prevHit.get(i) == j && prevHit.get(j) == i)) // otherwise they will continue to go into each other
          {
            continue;
          }
          
          if(shapes.get(i) instanceof Circle && shapes.get(j) instanceof Circle)
          {
//            System.out.println("Circles");
            if(((Circle) shapes.get(i)).areTouching((Circle) (shapes.get(j)))) // collision
            {
              prevHit.set(j, i);
              prevHit.set(i, j);
//              System.out.printf("i: %d j: %d\n", i, j);
//              System.out.println("Collision!");
              Shape.swapMomentum((Circle) shapes.get(i), (Circle) shapes.get(j));
              break;
            }
          }
        }
      }
      
      for(int i = 0; i < shapes.size(); i++)
      {
        if(i == selected)
          continue;
        Vector temp = shapes.get(i).getVelocity();
        int sign = (temp.getX() < 0 ? -1 : 1);
        temp.setX(sign * (Math.max(0, Math.abs(temp.getX()) - FRICTION * shapes.get(i).getArea())));
        sign = (temp.getY() < 0 ? -1 : 1);
        temp.setY(sign * (Math.max(0, Math.abs(temp.getY()) - FRICTION * shapes.get(i).getArea())));
        shapes.get(i).setVelocity(temp);
        moveShape(i);
        
        if(temp.getX() == Float.NaN || temp.getY() == Float.NaN)
        {
          System.out.println("Infinite velocity!");
        }
      }
      
      stopSelectedMovement();
      repaint();
    }
  }
  
  // Moves a circle after checking for boundary collisions
  private void moveShape(int index){
    Shape c = shapes.get(index);
    int width = (int) getWidth();
    int height = (int) getHeight();
    Vector velocity = c.getVelocity();
    // Check for boundaries and reverse direction
    // if necessary
    if(c.minX() <= 0)
    {
      velocity.setX(Math.abs(velocity.getX()));
      prevHit.set(index, -1);
      
      if(c.maxX() <= 0)
      {
        velocity.setX(velocity.getX() / 2);
              
      System.out.println("Out of bounds!");
      }
    }
    if(c.maxX() >= width)
    {
      velocity.setX(-1 * Math.abs(velocity.getX()));
      prevHit.set(index, -1);
      
      if(c.minX() >= width)
      {
        velocity.setX(velocity.getX() / 2);
        
      System.out.println("Out of bounds!");
      }
    }
    if(c.minY() <= 0) 
    {
      velocity.setY(Math.abs(velocity.getY()));
      prevHit.set(index, -1);
      
      if(c.maxY() <= 0)
      {
        velocity.setX(velocity.getX() / 2);
              
      System.out.println("Out of bounds!");
      }
    }
    if(c.maxY() >= height)
    {
      velocity.setY(-1  * Math.abs(velocity.getY()));
      prevHit.set(index, -1);
      
      if(c.minY() >= height)
      {
        velocity.setX(velocity.getX() / 2);
              
      System.out.println("Out of bounds!");
      }
    }
    
    c.move();
  }
  
  // Stops or restarts the timer if the user presses the mouse
  public class UserListener extends MouseAdapter{
    public void mousePressed(MouseEvent e){
      
      x = e.getX(); 
      y = e.getY();
      
      for(int i = 0; i < shapes.size(); i++)
      {
        if(shapes.get(i).containsPoint(x, y))
        {
//          System.out.println(i);
          selected = i;
          stopSelectedMovement();
          break;
        }
      }

      if(selected == -1)
      {
        if (timer.isRunning())
          timer.stop();
        else
          timer.restart();
      }
      
    }
    
    public void mouseReleased(MouseEvent e)
    {
      selected = -1;
      x = e.getX();
      y = e.getY();
      
      System.out.println("Shape unselected");
    }
  }
  
  private class PanelMotionListener extends MouseMotionAdapter{
    
    public void mouseDragged(MouseEvent e){
      int newX = e.getX();
      int newY = e.getY();
      int dx = newX - x;
      int dy = newY - y;
      if (selected != -1)
      {
        shapes.get(selected).setVelocity(new Vector());
        shapes.get(selected).setVelocity(new Vector(dx, dy));
//        System.out.println(selected instanceof Circle);
//        System.out.println("Moved");
        moveShape(selected);
      }
      x = newX;
      y = newY; 
      repaint();
    }
  }
  
}