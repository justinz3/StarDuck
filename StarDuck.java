// Description: Run this to Run StarDuck
// Created: 3/2/19

import Panels.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StarDuck implements JavaArcade {

    public static void main(String[] args) throws IOException {
        JFrame theGUI = new JFrame();
        theGUI.setTitle("StarDuck");
        theGUI.setSize(900, 500);
        theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StarDuckControlPanel controlPanel = new StarDuckControlPanel(theGUI);
        Container pane = theGUI.getContentPane();
        pane.add(controlPanel);
        theGUI.setVisible(true);
    }

    public boolean running() {
        return false;
    }
    public void startGame() {

    }
    public String getGameName() {
        return "StarDuck";
    }
    public void pauseGame() {

    }
    public String getInstructions() {
        return "INSTRUCTIONS";
    }
    public String getCredits() {
        return "CREDITS Dan and Justin";
    }
    public String getHighScore() {
        return "High Score: 0";
    }
    public void stopGame() {

    }
    public int getPoints() {
        return 0;
    }
}
