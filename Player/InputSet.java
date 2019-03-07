// A generalized input set which contains both keyboard and mouse inputs
// Created 3-6-19

package Player;

import java.awt.*;
import java.awt.event.MouseEvent;

public enum InputSet {

    WASD_MOUSE(KeyMovementSet.WASD, (MouseEvent) MouseEvent.BUTTON1);

    private KeyMovementSet movement;
    private Event fire;

    InputSet(KeyMovementSet movement, Event fire) {
        this.movement = movement;
        this.fire = fire;
    }
}
