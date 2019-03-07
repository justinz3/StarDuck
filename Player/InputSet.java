// A generalized input set which contains both keyboard and mouse inputs
// Created 3-6-19

package Player;

import java.awt.*;
import java.awt.event.MouseEvent;

public enum InputSet {

    WASD_MOUSE(KeyMovementSet.WASD, MouseEvent.BUTTON1);

    private KeyMovementSet movement;
    private int fireKey;
    private boolean useMouse;

    InputSet(KeyMovementSet movement, int fire, boolean useMouse) {
        this.movement = movement;
        this.fireKey = fire;
        this.useMouse = useMouse;
    }

    InputSet(KeyMovementSet movement, int fire) {
        this(movement, fire, true);
    }
}
