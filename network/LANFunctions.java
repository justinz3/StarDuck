package network;

import Player.KeyInputSet;

import java.util.HashMap;

public interface LANFunctions {
    boolean checkConnection() throws Exception;
    void sendMessage(String message) throws Exception;
    String receiveMessage() throws Exception;

    /**
     * Gets the current WASD state from the corresponding game
     * @return An array of booleans in the following order: [W, A, S, D, Space, Shift, game paused]
     * @throws Exception
     */
    boolean[] getKeyboardStatus() throws Exception;

    void sendKeyboardStatus(HashMap<Integer, Boolean> keyboardMap, KeyInputSet inputSet) throws Exception;
}
