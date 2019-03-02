

package Panels;

import javax.swing.*;

public class ControlPanel extends JPanel implements JavaArcade {

    public boolean running() {
        return false;
    }
    public void startGame() {

    }
    public String getGameName() {
        return "StarDuck";
    }
    public void pauseGame() {

    }
    public String getInstructions() {
        return "INSTRUCTIONS";
    }
    public String getCredits() {
        return "CREDITS Dan and Justin";
    }
    public String getHighScore() {
        return "High Score: 0";
    }
    public void stopGame() {

    }
    public int getPoints() {
        return 0;
    }
}
