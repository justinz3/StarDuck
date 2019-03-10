// Description: Contains various keyboard input sets (WASD, arrow keys, etc.)
// Created: 3/3/19

package Player;

import java.awt.event.KeyEvent;

public enum KeyInputSet {

    WASD (KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT, false),
    ARROW_KEYS (KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT, false),
    NUMPAD (KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, false);


    private int forward, left, backward, right;
    private int primaryShoot, secondaryShoot; // TODO allow shooting
    private boolean useMouse;

    KeyInputSet(int forward, int left, int backward, int right, int primaryShoot, int secondaryShoot, boolean useMouse) {

        this.forward = forward;
        this.left = left;
        this.backward = backward;
        this.right = right;
        this.primaryShoot = primaryShoot;
        this.secondaryShoot = secondaryShoot;

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
