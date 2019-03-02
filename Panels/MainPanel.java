package Panels;

import javax.swing.*;
import java.awt.*;

import Physics.Circle;
import Physics.Drawable;
import Ship.Ship;

import java.util.*;

public class MainPanel extends JPanel {

    private ArrayList<Drawable> objects;
    private Image background;
    private final int BACKGROUND_WIDTH, BACKGROUND_HEIGHT;
    private int width, height;

    public MainPanel(int width, int height){
        setSize(width, height);
        setLayout(null);

        this.width = width;
        this.height = height;

        background = new ImageIcon("gifs/stars.gif").getImage();
        BACKGROUND_WIDTH = 786;
        BACKGROUND_HEIGHT = 456;
    }


    private Circle deleteMe = new Circle();
    private Ship deleteMeToo = new Ship();
    public void paintComponent(Graphics g){
        super.paintComponent(g);


        // TODO draw all ships


        /*setBackground(Color.BLUE);
        deleteMe.draw(g);
        deleteMe.move();
        System.out.printf("Pos: <%.2f, %.2f>\nVel: <%.2f, %.2f>\nAcc: <%.2f, %.2f>\n",
                deleteMe.getPosition().getX(), deleteMe.getPosition().getY(),
                deleteMe.getVelocity().getX(), deleteMe.getVelocity().getY(),
                deleteMe.getAcceleration().getX(), deleteMe.getAcceleration().getY());*/

        g.drawImage(background, (width - BACKGROUND_WIDTH) / 2, (height - BACKGROUND_HEIGHT) / 2, this);

        deleteMeToo.draw(g, this);
    }
}
