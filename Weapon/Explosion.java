package Weapon;

import Helpers.Helpers;
import Physics.Drawable;
import Physics.Vector;

import javax.swing.*;
import java.awt.*;

public class Explosion implements Drawable {

    Vector position;

    public Explosion (Vector position) {
        this.position = position;
    }

    public void draw(Graphics g) {
        while(!g.drawImage(Helpers.getImage("graphics/missile-explosion.gif"), (int) position.getX(), (int) position.getY(), null))
            continue;
    }

    public void draw(Graphics g, JPanel observer) {
        while(!g.drawImage(Helpers.getImage("graphics/missile-explosion.gif"), (int) position.getX(), (int) position.getY(), observer))
            continue;
    }
}
