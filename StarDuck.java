// Description: Run this to Run StarDuck
// Created: 3/2/19

import Panels.MainMenuPanel;
import Panels.StarDuckControlPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StarDuck {

    public static void main(String[] args) throws IOException {
        JFrame theGUI = new JFrame();
        theGUI.setTitle("StarDuck");
        theGUI.setSize(786, 456 + 32);
        theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theGUI.setResizable(false);
        StarDuckControlPanel panel = new StarDuckControlPanel(theGUI);
        Container pane = theGUI.getContentPane();
        pane.add(panel);
        theGUI.setVisible(true);
    }
}
