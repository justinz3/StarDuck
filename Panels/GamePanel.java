package Panels;

import javax.swing.*;
import java.awt.*;

import Physics.*;
import Player.*;

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

    public GamePanel(int width, int height, StarDuckControlPanel.GameType gameType, StarDuckControlPanel controlPanel) {
        this.setFocusable(true);
        this.requestFocus();
        this.controlPanel = controlPanel;

        // Panel focusing code taken from: https://bit.ly/2EHLuHs
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                GamePanel.this.requestFocusInWindow();
            }
        });

        this.gameType = gameType;
        this.backgroundImage = getImage("graphics/stars-scrolling-large.gif");
        ;

        initPanelSize(width, height);

        //initMenu();

        initTimer();

        this.keyListener = new KeyboardListener();
        addKeyListener(keyListener);

        objects = new ArrayList<Drawable>();
        objects.add(new Player(new Ship(), KeyInputSet.WASD, true));
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

    private static Image getImage(String fileAddress) {
        return new ImageIcon(fileAddress).getImage();
    }

    // ---------------------------------------------------------


    private Circle deleteMe = new Circle();
    private Ship deleteMeToo = new Ship();

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        g.drawImage(backgroundImage, cornerDisplacementX, cornerDisplacementY, this);

        /*setBackground(Color.BLUE);
        deleteMe.draw(g);
        deleteMe.move();
        System.out.printf("Pos: <%.2f, %.2f>\nVel: <%.2f, %.2f>\nAcc: <%.2f, %.2f>\n",
                deleteMe.getPosition().getX(), deleteMe.getPosition().getY(),
                deleteMe.getVelocity().getX(), deleteMe.getVelocity().getY(),
                deleteMe.getAcceleration().getX(), deleteMe.getAcceleration().getY());*/


        for (Drawable object : objects) {
            object.draw(g, this);
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
            time += delay;
            for (Drawable object : objects) {
                if (object instanceof Player) {
                    Player player = (Player) object;
                    player.move();
                }
            }
        }
    }

    public void endGame() {
        removeKeyListener(keyListener);
        controlPanel.showMenu();
    }

    public class KeyboardListener implements KeyListener {

        public KeyboardListener() {
            super();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                endGame();

            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i) instanceof Player) {
                    Player player = (Player) objects.get(i);
                    if (e.getKeyCode() == player.input.getForward()) {
                        player.moveForward();
                    }
                    if (e.getKeyCode() == player.input.getBackward()) {
                        player.moveBackward();
                    }
                    if (e.getKeyCode() == player.input.getLeft()) {
                        if (player.isStrafing())
                            player.strafeLeft();
                        else
                            player.turnLeft();
                    }
                    if (e.getKeyCode() == player.input.getRight()) {
                        if (player.isStrafing())
                            player.strafeRight();
                        else
                            player.turnRight();
                    }

                    //System.out.println(player.getRotation());
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println(e);
            //System.out.println("Typed: " + e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i) instanceof Player) {
                    Player player = (Player) objects.get(i);
                    if (e.getKeyCode() == player.input.getForward()) {
                        player.stopForward();
                    }
                    if (e.getKeyCode() == player.input.getBackward()) {
                        player.stopBackward();
                    }
                    if (e.getKeyCode() == player.input.getLeft()) {
                        if (player.isStrafing())
                            player.stopLeft();
                        // turning stops on its own
                    }
                    if (e.getKeyCode() == player.input.getRight()) {
                        if (player.isStrafing())
                            player.stopRight();
                        // turning stops on its own
                    }

                    //System.out.println(player.getRotation());
                }
            }
        }
    }
}
