package Panels;

import javax.swing.*;
import java.awt.*;

import Physics.*;
import Player.*;

import java.awt.event.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;

public class MainPanel extends JPanel {

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

    private enum Location {
        MENU,

        LOCAL,

        LAN;
    }
    private Location location = Location.MENU;

    public MainPanel(int width, int height) {
        this.setFocusable(true);
        this.requestFocus();

        initPanelSize(width, height);

        initMenu();

        initTimer();

        addKeyListener(new KeyboardListener());

        objects = new ArrayList<Drawable>();
        objects.add(new Player(new Ship(), KeyInputSet.WASD, true));
    }

    private void initMenu() {

        initMenuBackground();

        initMenuButtons();

        //background.add(playLocal, 0);
        //background.add(playLAN, 0);
        //this.add(background);

        clickAreaListener = new ClickAreaListener();
        addMouseListener(clickAreaListener);
    }

    private void initMenuBackground() {

        backgroundWidth = 786;
        backgroundHeight = 456;

        cornerDisplacementX = (width - backgroundWidth) / 2;
        cornerDisplacementY = (height - backgroundHeight) / 2;
    }

    private void initMenuButtons() {
        playLocal = new Physics.Rectangle(new Physics.Vector(cornerDisplacementX + 20 + buttonWidth / 2,
                cornerDisplacementY + 170 + buttonHeight / 2), buttonWidth, buttonHeight);

        playLAN = new Physics.Rectangle(new Physics.Vector(cornerDisplacementX + 570 + buttonWidth / 2,
                cornerDisplacementY + 170 + buttonHeight / 2), buttonWidth, buttonHeight);
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

    private void clearMenu() {
        // clear the menu?
        removeMouseListener(clickAreaListener);

        playIntro = false;
    }


    // ---------------------------------------------------------




    private Circle deleteMe = new Circle();
    private Ship deleteMeToo = new Ship();
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //System.out.println(location);

        switch(location) {
            case MENU:
                if(time < introDuration && playIntro) {
                    backgroundImage = getImage("gifs/main-menu-intro.gif");
                }
                else {
                    backgroundImage = getImage("gifs/main-menu-loop.gif");
                }

                // Note: Intentional Feature - User can click buttons before they appear
                break;
            case LOCAL:
            case LAN:
                // Drawing should be the same for both LOCAL and LAN
                backgroundImage = getImage("gifs/stars-scrolling.gif");
                break;
        }

        g.drawImage(backgroundImage, cornerDisplacementX, cornerDisplacementY, this);

        g.setColor(Color.blue);
        //g.fillRect(cornerDisplacementX + 20, cornerDisplacementY + 170, 195, 115);
        //g.fillRect(cornerDisplacementX + 570, cornerDisplacementY + 170, 195, 115);
        // TODO draw all ships


        /*setBackground(Color.BLUE);
        deleteMe.draw(g);
        deleteMe.move();
        System.out.printf("Pos: <%.2f, %.2f>\nVel: <%.2f, %.2f>\nAcc: <%.2f, %.2f>\n",
                deleteMe.getPosition().getX(), deleteMe.getPosition().getY(),
                deleteMe.getVelocity().getX(), deleteMe.getVelocity().getY(),
                deleteMe.getAcceleration().getX(), deleteMe.getAcceleration().getY());*/



        //deleteMeToo.draw(g, this);
        switch(location) {
            case MENU:
                break;
            case LOCAL:
            case LAN:
                // Drawing should be the same for both LOCAL and LAN
                for(Drawable object : objects) {
                    object.draw(g, this);
                    if(object instanceof Player) {
                        Player player = (Player) object;
                        //System.out.printf("%s %s\n", player.getShip().getPosition(), player.getShip().getVelocity());
                    }
                }
                break;
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
            if(location != Location.MENU) {
                for(Drawable object : objects) {
                    if(object instanceof Player) {
                        Player player = (Player) object;
                        player.move();
                    }
                }
            }
        }
    }

    private class ClickAreaListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e){

            int x = e.getX();
            int y = e.getY();

            Physics.Vector point = new Physics.Vector(x, y);

            if(time < introDuration && playIntro) {
                return;
            }
            if(playLocal.containsPoint(point)) {
                location = Location.LOCAL;
                clearMenu();
            }
            if(playLAN.containsPoint(point)) {
                location = Location.LAN;
                clearMenu();
            }
        }

        public void mousePressed(MouseEvent e)
        {

        }

        public void mouseReleased(MouseEvent e)
        {

        }
    }

    public class KeyboardListener implements KeyListener {

        public KeyboardListener() {
            super();
            System.out.println("Created listener");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println(e);
            //System.out.println(e.getKeyCode());
            for(int i = 0; i < objects.size(); i++) {
                if(objects.get(i) instanceof Player) {
                    Player player = (Player) objects.get(i);
                    if(e.getKeyCode() == player.input.getForward()) {
                        player.moveForward();
                    }
                    if(e.getKeyCode() == player.input.getBackward()) {
                        player.moveBackward();
                    }
                    if(e.getKeyCode() == player.input.getLeft()) {
                        if(player.isStrafing())
                            player.strafeLeft();
                        else
                            player.turnLeft();
                    }
                    if(e.getKeyCode() == player.input.getRight()) {
                        if(player.isStrafing())
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
            for(int i = 0; i < objects.size(); i++) {
                if(objects.get(i) instanceof Player) {
                    Player player = (Player) objects.get(i);
                    if(e.getKeyCode() == player.input.getForward()) {
                        player.stopForward();
                    }
                    if(e.getKeyCode() == player.input.getBackward()) {
                        player.stopBackward();
                    }
                    if(e.getKeyCode() == player.input.getLeft()) {
                        if(player.isStrafing())
                            player.stopLeft();
                        // turning stops on its own
                    }
                    if(e.getKeyCode() == player.input.getRight()) {
                        if(player.isStrafing())
                            player.stopRight();
                        // turning stops on its own
                    }

                    //System.out.println(player.getRotation());
                }
            }
        }
    }
}
