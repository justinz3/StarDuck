// Description: A Hitbox made of shapes
// Created: 3/2/19

package Physics;

import java.awt.*;
import java.util.ArrayList;

public class Hitbox extends ArrayList<MovableShape> implements Movable {

    private Vector center;

    public Hitbox() {
        this(new Vector(0, 0)); // this one will require the client to set the center
    }

    public Hitbox(Vector center) {
        super();
        setCenter(center);
    }

    public boolean isTouching(Hitbox other) {
        for(int i = 0; i < size(); i++) {
            for(int j = 0; j < other.size(); j++) {
                if(i == j)
                    continue;
                //System.out.println(get(i) + " : " + other.get(j));
                if(get(i).isTouching(other.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void move() {
        for(int i = 0; i < size(); i++) {
            get(i).move();
        }
    }

    public void setRotation(int degrees) {
        for(int i = 0; i < size(); i++) {
            get(i).setRotation(degrees);
        }
    }

    public void setCenter(Vector center) {
        this.center = center;
        for(int i = 0; i < size(); i++) {
            get(i).setCenterOfRotation(center); // this way, if the center changes, it changes for all the shapes
        }
    }

    public Vector getCenter() {
        return center;
    }
}
