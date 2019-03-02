/*
 * Author: Justin Zhu
 * 
 * Description: Final Project class, It's the window for the game
 * 
 * Created: 6-18-18
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

// Mouse events from StackOverflow
//SwingUtilities.isLeftMouseButton(MouseEvent anEvent) 
//SwingUtilities.isRightMouseButton(MouseEvent anEvent) 
//SwingUtilities.isMiddleMouseButton(MouseEvent anEvent)

public class RTS extends JPanel{
  
  private ArrayList<Unit> units;
  private ArrayList<Projectile> projectiles;
  private ArrayList<Integer> selected;// Used to track unselected Units (tracks indices)
  private ArrayList<Rectangle> walls;
  private int x, y;                   // Used to track mouse coordinates  
  private javax.swing.Timer timer;
  private double time;
  private int playerTeam = 0;
  private Rectangle selectBox;
  private double ALERT_RANGE = 150;
  private String message = "";
  private boolean inMenu = true; // start in the menu
  private JButton playButton;
  private final int firstLevel = 0, lastLevel = 2;
  private int selectedLevel = 0;
  private JComboBox levelSelect;
  
  public RTS(int width, int height, int level) throws IOException{
    setSize(width, height);
    setLayout(null);
    
    // Init menu
    playButton = new JButton("Play");
    playButton.setVerticalTextPosition(AbstractButton.CENTER);
    playButton.setHorizontalTextPosition(AbstractButton.CENTER);
    playButton.setActionCommand("play");
//    Dimension temp = playButton.getPreferredSize();
    playButton.setBounds(getWidth() / 2 - 100, getHeight() / 2, (int) (Math.sqrt(2) * 50), 50);
    playButton.addActionListener(new AnimationListener());
    this.add(playButton);
    String[] levels = new String[lastLevel - firstLevel + 1];
    for(int i = 0; i < levels.length; i++)
    {
      levels[i] = "" + i;
    }
    levelSelect = new JComboBox(levels);
    levelSelect.setBounds(getWidth() / 2 - 100, getHeight() / 2 + 100, (int) (Math.sqrt(2) * 50), 50);
    levelSelect.addActionListener(new dropDownListener());
    this.add(levelSelect);
    
    units = new ArrayList<Unit>();
    walls = new ArrayList<Rectangle>();
    selected = new ArrayList<Integer>();
    projectiles = new ArrayList<Projectile>();
    selectBox = null;
    
    // more setup
    time = 0;
    timer = new javax.swing.Timer(5, new AnimationListener());
    timer.start();
    addMouseListener(new UserListener()); 
    addMouseMotionListener(new PanelMotionListener());
  }
  
  public void loadLevel(int level) throws IOException
  {
    if(level < firstLevel || level > lastLevel)
    {
      System.out.println("Invalid Level");
      System.exit(0);
    }
    File levelFile = new File(String.format("levels/level%d.txt", level));
    Scanner input = new Scanner(levelFile);
    
    int numUnits = input.nextInt();
    input.nextLine();
    for(int i = 0; i < numUnits; i++)
    {
      String unit = input.nextLine();
      String[] properties = unit.split(" ");
      int[] nums = new int[properties.length];
      for(int j = 0; j < properties.length; j++)
      {
        nums[j] = Integer.parseInt(properties[j]);
      }
      
      units.add(new Unit(nums[0], nums[1], nums[2], nums[3]));
    }
    
    int numWalls = input.nextInt();
    input.nextLine();
    for(int i = 0; i < numWalls; i++)
    {
      String wall = input.nextLine();
      String[] properties = wall.split(" ");
      int[] nums = new int[properties.length];
      for(int j = 0; j < properties.length; j++)
      {
        nums[j] = Integer.parseInt(properties[j]);
      }
      
      walls.add(new Rectangle(nums[0], nums[1], new Vector(), Color.gray, nums[2], nums[3]));
    }
  }
  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    if(inMenu)
    {
      drawMenu(g);
    }
    else
    {
      drawGame(g);
    }
  }
  
  public void drawGame(Graphics g)
  {
    setBackground(Color.green);
    for(int i = 0; i < units.size(); i++)
    {
      units.get(i).draw(g);
      if(selected.contains(i))
      {
        units.get(i).drawRange(g);
      }
    }
    
    for(int i = 0; i < projectiles.size(); i++)
    {
      projectiles.get(i).draw(g);
    }
    
    for(int i = 0; i < walls.size(); i++)
    {
      walls.get(i).fill(g);
    }
    
    if(selectBox != null)
      selectBox.draw(g);
    
    if(checkGameOver()) // game is over, so shade in screen
    {
      // The section with Graphics2D is copied from StackOverflow
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
      g2d.setColor(Color.black);
      g2d.fillRect(0, 0, getWidth(), getHeight());
      g2d.setColor(Color.white);
      Font font = new Font("serif", Font.PLAIN, 50); // large message
      g2d.setFont(font);
      g2d.drawString(message, getWidth() / 2 - 300, getHeight() / 2);
      
      Font instructions = new Font("serif", Font.PLAIN, 20);
      g2d.setFont(instructions);
      g2d.drawString("(Click to return to menu)", getWidth() / 2 - 100, getHeight() / 2 + 50);
    }
  }
  
  public void drawMenu(Graphics g)
  {
    setBackground(Color.white);
    Font title = new Font("serif", Font.PLAIN, 70);
    g.setFont(title);
    g.drawString("A Game", getWidth() / 2 - 175, getHeight() / 2 - 100);
    g.setFont(new Font("serif", Font.PLAIN, 10));
    g.drawString("by Justin Zhu", getWidth() / 2 - 50, getHeight() / 2 - 25);
    g.setFont(new Font("serif", Font.PLAIN, 20));
    g.drawString("Please select a level: ", getWidth() / 2 - 100, getHeight() / 2 + 75);
  }
  
  public void moveGameElements()
  {
    time += 5;
    
    if(checkGameOver())
    {
      timer.stop();
    }
    
    for(int i = 0; i < projectiles.size(); i++) // check if the projectiles hit anything
    {
      projectiles.get(i).move();
      
      if(projectiles.get(i).getX() > getWidth() || projectiles.get(i).getX() < 0 ||
         projectiles.get(i).getY() > getHeight() || projectiles.get(i).getY() < 0)
      {
        projectiles.remove(i);
        i--;
        continue;
      }
      
      boolean hasHit = false;
      
      for(int j = 0; j < walls.size(); j++)
      {
        if(walls.get(j).containsPoint(projectiles.get(i).getX(), projectiles.get(i).getY()))
        {
          projectiles.remove(i);
          hasHit = true;
          break;
        }
      }
      
      if(hasHit)
        continue;
      
      for(int j = 0; j < units.size(); j++)
      {
        if(projectiles.get(i).getTeam() == units.get(j).getTeam())
          continue;
        if(projectiles.get(i).hasHit(units.get(j)))
        {
          // triggers the unit that has been hit to respond
          int team = projectiles.get(i).getTeam();
          int index = getNearestUnit(team, units.get(j));
          // we don't want to cause an OutOfBounds error, or make the player's units move automatically
          if(index >= 0 && units.get(j).getTeam() != playerTeam) 
          {
            units.get(j).setTarget(units.get(index).getCircle().getCenterX(), 
                                   units.get(index).getCircle().getCenterY());
            // alert neighbors to the enemy
            double weaponRange = units.get(j).getWeapon().getRange();
            units.get(j).getWeapon().setRange(ALERT_RANGE);
            ArrayList<Unit> enemies = new ArrayList<Unit>();
            for(int k = 0; k < units.size(); k++)
            {
              if(units.get(k).getTeam() == units.get(j).getTeam() && units.get(j).inRange(units.get(k)))
              {
                enemies.add(units.get(k));
              }
            }
            units.get(j).getWeapon().setRange(weaponRange);
            
            Unit.setGroupTarget(enemies, units.get(j).getTargetX(), units.get(j).getTargetY());
          }
          
          projectiles.get(i).hit(units.get(j));
          projectiles.remove(i);
          i--;
          
          if(units.get(j).getHealth() <= 0)
          {
            if(selected.contains(j))
            {
              selected.remove(selected.indexOf(j));
            }
            else
            {
              System.out.println("Did not remove from selected");
            }
            units.remove(j);
            j--;
            
            for(int k = 0; k < selected.size(); k++)
            {
              if(selected.get(k) > j)
              {
                selected.set(k, selected.get(k) - 1);
              }
            }
          }
          hasHit = true;
          break;
        }
      }
    }
    
    
    for(int i = 0; i < units.size(); i++)
    {
      for(int j = 0; j < units.size(); j++)
      {
        if(i == j) 
        {
          continue;
        }
        
        if(units.get(i).getCircle().willTouch(units.get(j).getCircle()) &&
           (units.get(i).getVelocity().isMoving() || units.get(j).getVelocity().isMoving())) // if either is moving
        {
          // stop both of them
          units.get(i).setTarget(units.get(i).getCircle().getCenterX(), units.get(i).getCircle().getCenterY());
          units.get(j).setTarget(units.get(j).getCircle().getCenterX(), units.get(j).getCircle().getCenterY());
          units.get(i).setVelocity(new Vector());
          units.get(j).setVelocity(new Vector());
        }
        
        if(units.get(i).getTeam() == units.get(j).getTeam())
        {
          continue;
        }
        
        if(units.get(i).inRange(units.get(j)) && time >= units.get(i).getWeapon().getReload() + units.get(i).getLastAttack()) // i fires at j
        {
          projectiles.add(units.get(i).fireAt(units.get(j)));
          units.get(i).setLastAttack(time);
        }
        if(units.get(j).inRange(units.get(i)) && time >= units.get(j).getWeapon().getReload() + units.get(j).getLastAttack()) // j fires at i
        {
          projectiles.add(units.get(j).fireAt(units.get(i)));
          units.get(j).setLastAttack(time);
        }
      }
    }
    
    for(int i = 0; i < units.size(); i++)
    {
      moveUnit(i);
      
      if(units.get(i).getVelocity().getX() == Float.NaN || units.get(i).getVelocity().getY() == Float.NaN)
      {
        System.out.println("Infinite velocity!");
      }
    }
  }
  
  public void stopSelectedMovement()
  {
    for(int i = 0; i < selected.size(); i++)
    {
      Unit current = units.get(selected.get(i));
      current.setVelocity(new Vector());
      current.setTarget(current.getCircle().getCenterX(), current.getCircle().getCenterY());
    }
  }
  
  private class dropDownListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      selectedLevel = Integer.parseInt((String) ((JComboBox) e.getSource()).getSelectedItem());
    }
  }
  
  private class AnimationListener implements ActionListener{
    
    public void actionPerformed(ActionEvent e){
      
      if(inMenu)
      {
        // I looked at Oracle for how to do JButtons
        if("play".equals(e.getActionCommand()))
        {
          // Start the game
          inMenu = false;
          
          try
          {
            loadLevel(selectedLevel);
          }
          catch(IOException exception)
          {
            System.out.println("Level file not found");
            System.exit(0);
          }
          
          // remove the Play button
          removeButton(playButton);
          removeLevelSelect(levelSelect);
        }
      }
      else
      {
        moveGameElements();
      }
      
      repaint();
    }
  }
  
  private void removeButton(JButton in)
  {
    this.remove(in);
  }
  
  private void removeLevelSelect(JComboBox in)
  {
    this.remove(in);
  }
  
  private boolean checkGameOver()
  {
    return checkPlayerWon() || checkPlayerLost();
  }
  
  private boolean checkPlayerWon()
  {
    for(int i = 0; i < units.size(); i++)
    {
      if(units.get(i).getTeam() != playerTeam)
      {
        return false;
      }
    }
    displayMessage("You Are Victorious!");
    return true;
  }
  
  private boolean checkPlayerLost()
  {
    for(int i = 0; i < units.size(); i++)
    {
      if(units.get(i).getTeam() == playerTeam)
      {
        return false;
      }
    }
    displayMessage("You Have Been Defeated!");
    return true;
  }
  
  private void displayMessage(String message)
  {
    // System.out.println(message); // do this for now
    this.message = message;
  }
  
  // Moves a circle after checking for boundary collisions
  private void moveUnit(int index){
    Unit c = units.get(index);
    int width = (int) getWidth();
    int height = (int) getHeight(); // width and height of the window
    Vector velocity = c.getVelocity();
    // Check for boundaries and reverse direction
    // if necessary
    if(c.getCircle().minX() <= 0 || c.getCircle().maxX() >= width || 
       c.getCircle().minY() <= 0 || c.getCircle().maxY() >= height)
    {
      c.setVelocity(new Vector()); // stop when it hits the edge
      c.setTarget(c.getCircle().getCenterX(), c.getCircle().getCenterY());
      
      if(c.getCircle().minY() >= 0)
      {
//        System.out.println("Out of bounds!");
      }
    }
    
    for(int i = 0; i < walls.size(); i++)
    {
      if(walls.get(i).willTouch(new Rectangle(c.getCircle())))
      {
        c.setVelocity(new Vector());
        c.setTarget(c.getCircle().getCenterX(), c.getCircle().getCenterY());
        break;
      }
    }
    
    c.move();
  }
  
  public int getNearestUnit(int team, Unit subject)
  {
    int index = -1;
    double closest = -1;
    double posX = subject.getCircle().getCenterX();
    double posY = subject.getCircle().getCenterY();
    for(int i = 0; i < units.size(); i++)
    {
      if(units.get(i).getTeam() == team && units.get(i) != subject)
      {
        double dist = Math.hypot(units.get(i).getCircle().getCenterX() - posX, 
                                 units.get(i).getCircle().getCenterY() - posY);
        if(dist < closest || closest == -1)
        {
          closest = dist;
          index = i;
        }
      }
    }
    
    return index;
  }
  
  // Stops or restarts the timer if the user presses the mouse
  public class UserListener extends MouseAdapter{
    public void mouseClicked(MouseEvent e){
      
      x = e.getX(); 
      y = e.getY();
      
      if(SwingUtilities.isLeftMouseButton(e)) // user wants to select a unit
      {
        selected.clear();
        for(int i = 0; i < units.size(); i++)
        {
          if(units.get(i).getCircle().containsPoint(x, y) && units.get(i).getTeam() == playerTeam)
          {
            selected.add(i);
          }
        }
        stopSelectedMovement();
      }
      
      if(SwingUtilities.isRightMouseButton(e)) // user wants to move a unit
      {
        ArrayList<Unit> current = new ArrayList<Unit>();
        for(int i = 0; i < selected.size(); i++)
        {
          current.add(units.get(selected.get(i)));
        }
        Unit.setGroupTarget(current, x, y);
      }
      
      if(checkGameOver())
      {
        inMenu = true; // return to menu when the game is over
      }
      
      return;
    }
    
    public void mousePressed(MouseEvent e)
    {
      if(SwingUtilities.isLeftMouseButton(e))
      {
        selectBox = new Rectangle(); // initialize it, the user is dragging
        selectBox.setWidth(0);
        selectBox.setHeight(0);
        selectBox.setLeftX(x);
        selectBox.setTopY(y);
//        System.out.println(selectBox);
        
        x = e.getX();
        y = e.getY();
      }
    }
    
    public void mouseReleased(MouseEvent e)
    {
      if(selectBox != null) // user is trying to select multple units
      {
        selected.clear();
        for(int i = 0; i < units.size(); i++)
        {
          Rectangle hitbox = new Rectangle(units.get(i).getCircle());
          if(units.get(i).getTeam() == playerTeam && selectBox.isTouching(hitbox))
          {
            selected.add(i);
          }
        }
      }
      selectBox = null;
    }
    
  }
  
  private class PanelMotionListener extends MouseMotionAdapter{
    
    public void mouseDragged(MouseEvent e){
      int newX = e.getX();
      int newY = e.getY();
      if(selectBox != null)
      {
        selectBox.setLeftX(x);
        selectBox.setTopY(y);
        selectBox.stretchToX(newX);
        selectBox.stretchToY(newY);
//        System.out.println(selectBox);
      }
      
      repaint();
    }
  }
  
}