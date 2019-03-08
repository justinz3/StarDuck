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

    private ArrayList<Drawable> objects;

    private JLayeredPane background;
    private Image backgroundImage;
    private int backgroundWidth, backgroundHeight;
    private int width, height;
    private int cornerDisplacementX, cornerDisplacementY;

    private Physics.Rectangle playLocal, playLAN;
    private final int buttonWidth = 195, buttonHeight = 115;
    private MouseAdapter clickAreaListener;

    private final int introDuration = 13750;
    private boolean playIntro = false; // TODO change to true

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

        objects = new ArrayList<Drawable>();
        objects.add(new Player(new Ship(this.getSize()), KeyInputSet.WASD, false));

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

    // ---------------------------------------------------------

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        g.drawImage(backgroundImage, cornerDisplacementX, cornerDisplacementY, this);

        for (Drawable object : objects) {
            object.draw(g);
            if (object instanceof Player) {
                Player player = (Player) object;
                //System.out.printf("%s %s\n", player.getShip().getPosition(), player.getShip().getVelocity());
            }
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
                for (int i = 0; i < objects.size(); i++) {
                    if (objects.get(i) instanceof Player) {
                        Player player = (Player) objects.get(i);
                        if (isPressed.getOrDefault(player.input.getForward(), false))
                            player.moveForward();
                        else if (isPressed.getOrDefault(player.input.getBackward(), false))
                            player.moveBackward();
                        else
                            player.zeroVelocity();

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
                            Projectile proj = player.fire();
                            if (proj != null)
                                objects.add(proj);
                        }
                    }
                }

                time += delay;
                ArrayList<Drawable> objectsToRemove = new ArrayList<>();
                for (Drawable object : objects) {
                    object.move();
                    if(object instanceof Player)
                        ((Player) object).updateTimeSinceLastFire(delay);
                    else if(object instanceof Projectile) {
                        Vector position = ((Projectile) object).getPosition();
                        if(position.getX() <= -30 || position.getY() <= -30 || position.getX() >= getWidth() + 30 || position.getY() >= getHeight() + 30)
                            objectsToRemove.add(object);
                    }
                }

                // Avoids ConcurrentModificationExceptions
                for(Drawable object : objectsToRemove)
                    objects.remove(object);
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
            if (gameType == StarDuckControlPanel.GameType.LAN) {
                int x = e.getX();
                int y = e.getY();

                int n = objects.size();
                for (int i = 0; i < n; i++) {
                    Drawable object = objects.get(i);
                    if (object instanceof Player) {
                        Player player = (Player) object;
                        Projectile proj = player.getShip().fire();
                        objects.add(proj);
                    }
                }
            }
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
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Player) {
                Player player = (Player) objects.get(i);
                player.setVelocity(new Vector(0, 0));
            }
        }
    }
}
