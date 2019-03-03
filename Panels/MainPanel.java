package Panels;

import javax.swing.*;
import java.awt.*;

import Physics.Circle;
import Physics.Drawable;
import Physics.Rectangle;
import Physics.Vector;
import Player.Ship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class MainPanel extends JPanel {

    private ArrayList<Drawable> objects;

    private JLayeredPane background;
    private Image backgroundImage;
    private int backgroundWidth, backgroundHeight;
    private int width, height;
    private int cornerDisplacementX, cornerDisplacementY;

    private Rectangle playLocal, playLAN;
    private final int buttonWidth = 195, buttonHeight = 115;
    private MouseAdapter clickAreaListener;

    private final int introDuration = 13750;

    private int time;
    private javax.swing.Timer timer;
    private int delay = 5;

    private enum Location {
        MENU,

        LOCAL,

        LAN;
    }
    private Location location = Location.MENU;

    public MainPanel(int width, int height){
        initPanelSize(width, height);

        initMenu();

        initTimer();
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
        background = new JLayeredPane();

        backgroundWidth = 786;
        backgroundHeight = 456;

        cornerDisplacementX = (width - backgroundWidth) / 2;
        cornerDisplacementY = (height - backgroundHeight) / 2;
    }

    private void initMenuButtons() {
        playLocal = new Rectangle(new Vector(cornerDisplacementX + 20 + buttonWidth / 2,
                cornerDisplacementY + 170 + buttonHeight / 2), buttonWidth, buttonHeight);

        playLAN = new Rectangle(new Vector(cornerDisplacementX + 570 + buttonWidth / 2,
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
    }


    // ---------------------------------------------------------




    private Circle deleteMe = new Circle();
    private Ship deleteMeToo = new Ship();
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println(location);

        switch(location) {
            case MENU:
                if(time < introDuration) {
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
                backgroundImage = getImage("gifs/stars.gif");
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

    }




    // ---------------------------------------------------------




    private class TimerListener implements ActionListener {

        private int delay;

        public TimerListener(int delay) {
            this.delay = delay;
        }

        public void actionPerformed(ActionEvent e) {
            time += delay;
        }
    }

    private class ClickAreaListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e){

            int x = e.getX();
            int y = e.getY();

            Vector point = new Vector(x, y);
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
}
