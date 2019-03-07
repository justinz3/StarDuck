// Description: Contains various keyboard input sets (WASD, arrow keys, etc.)
// Created: 3/3/19

package Player;

import java.awt.event.KeyEvent;

public enum KeyMovementSet {

    WASD (KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D);


    private int forward, left, backward, right;
    private int primaryShoot, secondaryShoot; // TODO allow shooting
    KeyMovementSet(int forward, int left, int backward, int right) {

        this.forward = forward;
        this.left = left;
        this.backward = backward;
        this.right = right;
    }

    public int getForward() {
        return forward;
    }

    public int getLeft() {
        return left;
    }

    public int getBackward() {
        return backward;
    }

    public int getRight() {
        return right;
    }

    public int getPrimaryShoot() {
        return primaryShoot;
    }

    public int getSecondaryShoot() {
        return secondaryShoot;
    }


}
