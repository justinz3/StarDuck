

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GUIWindow {

   public static void main(String[] args) throws IOException{
      JFrame theGUI = new JFrame();
      theGUI.setTitle("A Game by Justin Zhu");
      theGUI.setSize(1000, 750); 
      theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      RTS panel = new RTS(1000, 750, 0);
      Container pane = theGUI.getContentPane();
      pane.setLayout(new GridLayout(1, 1));
      pane.add(panel);
      theGUI.setVisible(true);
   }
}