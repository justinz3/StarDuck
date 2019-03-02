// Description: Run this to Run StarDuck
// Created: 3/2/19

import Panels.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StarDuck {

    public static void main(String[] args) throws IOException {
        JFrame theGUI = new JFrame();
        theGUI.setTitle("StarDuck");
        theGUI.setSize(1000, 750);
        theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainPanel panel = new MainPanel(1000, 750);
        Container pane = theGUI.getContentPane();
        pane.setLayout(new GridLayout(1, 1));
        pane.add(panel);
        theGUI.setVisible(true);
    }
}
