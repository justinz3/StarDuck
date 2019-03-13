package network;

import Player.KeyInputSet;

import java.util.HashMap;

public class KeyBoardMapper {
    public String getKeyboardData(HashMap<Integer, Boolean> keyboardMap, KeyInputSet inputSet) {
        String data = "";

        String forward = getBitFromAction(keyboardMap, inputSet.getForward()), left = getBitFromAction(keyboardMap, inputSet.getLeft());

        data += forward + left;
        data += (forward.equals("0") ? getBitFromAction(keyboardMap, inputSet.getBackward()) : "0") + (left.equals("0") ? getBitFromAction(keyboardMap, inputSet.getRight()) : "0");
        data += getBitFromAction(keyboardMap, inputSet.getPrimaryShoot());
        data += getBitFromAction(keyboardMap, inputSet.getSecondaryShoot());

        return data;
    }

    private String getBitFromAction(HashMap<Integer, Boolean> keyMap, int action) {
        return keyMap.getOrDefault(action, false) ? "1" : "0";
    }

    public boolean[] interpretKeyboardData(String data) {
        String[] stringData = data.split("");
        boolean[] retData = new boolean[stringData.length];

        for (int i = 0; i < stringData.length; i++)
            retData[i] = Integer.parseInt(stringData[i]) == 1;

        return retData;
    }
}
