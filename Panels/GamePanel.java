package Panels;

import javax.swing.*;
import java.awt.*;

import Physics.*;
import Physics.Vector;
import Player.*;
import Weapon.*;
import Helpers.*;

import java.awt.event.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;

public class GamePanel extends JPanel {

    private ArrayList<Interactable> objects;
    private ArrayList<Player> players;
    public static ArrayList<Interactable> toBeAdded;

    private Image backgroundImage;
    private int width, height;
    private int cornerDisplacementX, cornerDisplacementY;

    private int time;
    private javax.swing.Timer timer;
    private int delay = 5;
    private StarDuckControlPanel.GameType gameType;
    private StarDuckControlPanel controlPanel;
    private KeyboardListener keyListener;
    private HashMap<Integer, Boolean> isPressed;

    private MouseListener mouseListener;
    private boolean running;

    public GamePanel(int width, int height, StarDuckControlPanel.GameType gameType, StarDuckControlPanel controlPanel) {
        this.setFocusable(true);
        this.requestFocus();
        this.controlPanel = controlPanel;

        getFocus();

        this.gameType = gameType;
        this.backgroundImage = Helpers.getImage("graphics/stars-scrolling-large.gif");

        initPanelSize(width, height);

        //initMenu();

        initTimer();

        this.keyListener = new KeyboardListener();
        addKeyListener(keyListener);

        this.mouseListener = new ClickListener();
        addMouseListener(mouseListener);

        toBeAdded = new ArrayList<>();
        objects = new ArrayList<>();
        players = new ArrayList<Player>();
        addPlayer(new Player(new Ship(Ship.ShipType.BLUE, this.getSize(), 1), KeyInputSet.WASD, false));
        addPlayer(new Player(new Ship(new Vector(width - 300, height - 300), new Vector(), new Vector(),
                new Laser(2), new Bomb(2), Ship.ShipType.GREEN, this.getSize(), 2),
                KeyInputSet.NUMPAD, false));

        running = true;

        isPressed = new HashMap<>();
    }

    private void initPanelSize(int width, int height) {
        setSize(width, height);
        setLayout(null);

        this.width = width;
        this.height = height;
    }

    private void initTimer() {
        time = 0;
        timer = new javax.swing.Timer(delay, new TimerListener(delay));
        timer.start();
    }

    private void addPlayer(Player player) {
        players.add(player);
        objects.add(player.getShip());
    }

    // ---------------------------------------------------------

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        g.drawImage(backgroundImage, cornerDisplacementX, cornerDisplacementY, this);

        for (Drawable object : objects) {
            object.draw(g);
        }
    }


    // ---------------------------------------------------------


    private class TimerListener implements ActionListener {

        private int delay;

        public TimerListener(int delay) {
            this.delay = delay;
        }

        public void actionPerformed(ActionEvent e) {
            if (isRunning()) {
                time += delay;

                // Clear dead players
                for(int i = 0; i < players.size(); i++) {
                    if(players.get(i).getShip().isDead()) {
                        players.remove(players.get(i));
                        i--;
                    }
                }

                // Clear destroyed objects
                for(int i = 0; i < objects.size(); i++) {
                    if(objects.get(i).isDead()) {
                        objects.remove(i);
                        i--;
                    }
                }

                // Add created objects
                while(toBeAdded.size() > 0) {
                    objects.add(toBeAdded.get(0));
                    toBeAdded.remove(0);
                }

                // Set Player movement
                for (Player player : players) {

                    if (isPressed.getOrDefault(player.input.getForward(), false))
                        player.moveForward();
                    else if (isPressed.getOrDefault(player.input.getBackward(), false))
                        player.moveBackward();
                    else {
                        player.zeroVelocity();
                    }

                    if (isPressed.getOrDefault(player.input.getLeft(), false)) {
                        if (player.isStrafing())
                            player.strafeLeft();
                        else
                            player.turnLeft();
                    } else if (isPressed.getOrDefault(player.input.getRight(), false)) {
                        if (player.isStrafing())
                            player.strafeRight();
                        else
                            player.turnRight();
                    }

                    if (isPressed.getOrDefault(player.input.getPrimaryShoot(), false)) {
                        Projectile proj = player.fire(0);
                        if (proj != null)
                            objects.add(proj);
                    }

                    if (isPressed.getOrDefault(player.input.getSecondaryShoot(), false)) {
                        Projectile proj = player.fire(1);
                        if (proj != null)
                            objects.add(proj);
                    }

                    player.move();
                }

                for (Player player : players) {
                    player.updateTimeSinceLastFire(delay);
                    player.updateTimeSinceLastDamage(delay);
                }

                // Move everything and Check for projectiles outside the screen
                ArrayList<Interactable> objectsToRemove = new ArrayList<>();
                for (Interactable object : objects) {
                    object.move();

                    if (object instanceof Projectile) {
                        Projectile projectile = (Projectile) object;
                        Vector position = projectile.getPosition();
                        Dimension size = projectile.getImageSize();

                        if (position.getX() <= -1 * size.getWidth() || position.getY() <= -1 * size.getHeight() ||
                                position.getX() >= getWidth() + size.getWidth() || position.getY() >= getHeight() + size.getHeight())
                            objectsToRemove.add(object);
                    }
                }

                // Avoids ConcurrentModificationExceptions
                for (Interactable object : objectsToRemove)
                    objects.remove(object);


                // Check for Collisions
                for (int i = 0; i < objects.size(); i++) {
                    for (int j = 0; j < objects.size(); j++) {
                        if (i == j)
                            continue;
                        if(i < 0)
                            break;
                        if(j < 0)
                            continue;

                        //System.out.println(i + " " + j);
                        Hittable a = objects.get(i), b = objects.get(j);

                        if (a.getTeam() == b.getTeam())
                            continue;

                        if (a.getHitbox().isTouching(b.getHitbox())) {

                            boolean bIsDead = a.impact(b);
                            boolean aIsDead = b.impact(a);

                            int deltaI = 0, deltaJ = 0;
                            if (bIsDead) {
                                objects.remove(b);
                                if (i > j)
                                    deltaI--;
                                deltaJ--;
                            }
                            if (aIsDead) {
                                objects.remove(a);
                                if (j > i)
                                    deltaJ--;
                                deltaI--;
                            }

                            i += deltaI;
                            j += deltaJ;
                        }
                    }

                }

            }

        }
    }

    public void endGame() {
        removeKeyListener(keyListener);
        controlPanel.showMenu();
        running = false;
    }

    public class KeyboardListener implements KeyListener {

        public KeyboardListener() {
            super();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                endGame();

            if (running) {
                isPressed.put(e.getKeyCode(), true);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println(e);
            //System.out.println("Typed: " + e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            isPressed.put(e.getKeyCode(), false);
        }
    }

    private class ClickListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
//            if (gameType == StarDuckControlPanel.GameType.LAN) {
//                int x = e.getX();
//                int y = e.getY();
//
//                int n = objects.size();
//                for (int i = 0; i < n; i++) {
//                    Drawable object = objects.get(i);
//                    if (object instanceof Player) {
//                        Player player = (Player) object;
//                        Projectile proj = player.getShip().fire();
//                        objects.add(proj);
//                    }
//                }
//            }
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }
    }

    public boolean isRunning() {
        return running;
    }

    public void getFocus() {
        // Panel focusing code taken from: https://bit.ly/2EHLuHs
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                GamePanel.this.requestFocusInWindow();
            }
        });
    }

    public void togglePause() {
        running = !running;
        removeKeyListener(keyListener);
        if (running) {
            getFocus();
            addKeyListener(keyListener);
        }
        stopAllPlayers(); // Fixes bug where holding a key then pausing and resuming makes the ship move in one direction indefinitely
    }

    private void stopAllPlayers() {
        for (Player player : players) {
            player.setVelocity(new Vector(0, 0));
        }
    }
}
