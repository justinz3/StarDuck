package Panels;

import javax.swing.*;
import java.awt.*;

import Physics.Circle;
import Physics.Drawable;
import Player.Ship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MainPanel extends JPanel {

    private ArrayList<Drawable> objects;

    private JLayeredPane background;
    private Image backgroundImage;
    private int backgroundWidth, backgroundHeight;
    private int width, height;
    private int cornerDisplacementX, cornerDisplacementY;

    private JButton playLocal, playLAN;
    private final int buttonWidth = 195, buttonHeight = 115;

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
    }

    private void initMenuBackground() {
        background = new JLayeredPane();

        backgroundWidth = 786;
        backgroundHeight = 456;

        cornerDisplacementX = (width - backgroundWidth) / 2;
        cornerDisplacementY = (height - backgroundHeight) / 2;
    }

    private void initMenuButtons() {
        playLocal = new JButton("Play Local Game");
        playLocal.setVerticalTextPosition(AbstractButton.CENTER);
        playLocal.setHorizontalTextPosition(AbstractButton.CENTER);
        playLocal.setActionCommand("local");
        playLocal.setBounds(cornerDisplacementX + 20, cornerDisplacementY + 170, buttonWidth, buttonHeight);
        playLocal.addActionListener(new ButtonListener());
        //this.add(playLocal);

        playLAN = new JButton("Play LAN Game");
        playLAN.setVerticalTextPosition(AbstractButton.CENTER);
        playLAN.setHorizontalTextPosition(AbstractButton.CENTER);
        playLAN.setActionCommand("lan");
        playLAN.setBounds(cornerDisplacementX + 570, cornerDisplacementY + 170, buttonWidth, buttonHeight);
        playLAN.addActionListener(new ButtonListener());
        //this.add(playLAN);
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
        removeButton(playLocal);
        removeButton(playLAN);
    }

    private void removeButton(JButton in)
    {
        this.remove(in);
    }

    private void removeComboBox(JComboBox in)
    {
        this.remove(in);
    }


    // ---------------------------------------------------------




    private Circle deleteMe = new Circle();
    private Ship deleteMeToo = new Ship();
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        switch(location) {
            case MENU:
                if(time < introDuration) {
                    backgroundImage = getImage("gifs/main-menu-intro.gif");
                }
                else {
                    backgroundImage = getImage("gifs/main-menu-loop.gif");
                }

                // Note: Intentional Feature - User can click buttons before they appear
                playLocal.paint(g);
                playLAN.paint(g);
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

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if("local".equals(e.getActionCommand())) {
                System.out.println("Local");
                location = Location.LOCAL;
                clearMenu();
            }
            if("lan".equals(e.getActionCommand())) {
                System.out.println("LAN");
                location = Location.LAN;
                clearMenu();
            }
        }
    }
}
