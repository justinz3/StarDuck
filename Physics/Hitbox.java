// Description: A Hitbox made of shapes
// Created: 3/2/19

package Physics;

import java.util.ArrayList;

public class Hitbox extends ArrayList<Shape> {

    public boolean isTouching(Hitbox other) {
        for(int i = 0; i < size(); i++) {
            for(int j = 0; j < other.size(); j++) {
                if(get(i).isTouching(other.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }
}
