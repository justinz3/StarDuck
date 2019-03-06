package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {

    private Image backgroundImage;
    private int backgroundWidth, backgroundHeight;
    private int width, height;
    private int cornerDisplacementX, cornerDisplacementY;

    private Physics.Rectangle playLocal, playLAN;
    private final int buttonWidth = 195, buttonHeight = 115;
    private MouseAdapter clickAreaListener;

    private final int introDuration = 11000; // How long the introduction of the main menu is (how long to wait until displaying main-menu-loop.gif)
    private boolean playIntro = false;

    private int time;
    private javax.swing.Timer timer;
    private int delay = 5;
    private StarDuckControlPanel controlPanel;

    public MainMenuPanel(int width, int height, StarDuckControlPanel controlPanel) {
        this.setFocusable(true);
        this.requestFocus();

        initPanelSize(width, height);

        initMenu();

        initTimer();
        this.controlPanel = controlPanel;
    }

    private void initMenu() {

        initMenuBackground();

        initMenuButtons();

        clickAreaListener = new MainMenuPanel.ClickAreaListener();
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
        timer = new javax.swing.Timer(delay, new MainMenuPanel.TimerListener(delay));
        timer.start();
    }

    private static Image getImage(String fileAddress) {
        return new ImageIcon(fileAddress).getImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        if (time < introDuration && playIntro) {
            backgroundImage = getImage("gifs/main-menu-intro.gif");
        } else {
            backgroundImage = getImage("gifs/main-menu-loop.gif");
            playIntro = false;
        }

        g.drawImage(backgroundImage, cornerDisplacementX, cornerDisplacementY, this);
    }

    public void clearMouse() {
        removeMouseListener(clickAreaListener);
    }
    public void reactivate() {
        addMouseListener(clickAreaListener);
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

        public void mouseClicked(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            Physics.Vector point = new Physics.Vector(x, y);

            if (time < introDuration && playIntro) {
                return;
            }
            if (playLocal.containsPoint(point)) {
                clearMouse();
                controlPanel.startGame(StarDuckControlPanel.GameType.LOCAL);
            }  else if (playLAN.containsPoint(point)) {
                clearMouse();
                controlPanel.startGame(StarDuckControlPanel.GameType.LAN);
            }
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }
    }
}
