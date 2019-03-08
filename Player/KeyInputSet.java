// Description: Contains various keyboard input sets (WASD, arrow keys, etc.)
// Created: 3/3/19

package Player;

import java.awt.event.KeyEvent;

public enum KeyInputSet {

    WASD (KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_SPACE, true);


    private int forward, left, backward, right;
    private int primaryShoot, secondaryShoot; // TODO allow shooting
    private boolean useMouse;

    KeyInputSet(int forward, int left, int backward, int right, int primaryShoot, boolean useMouse) {

        this.forward = forward;
        this.left = left;
        this.backward = backward;
        this.right = right;
        this.primaryShoot = primaryShoot;

        this.useMouse = useMouse;
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
