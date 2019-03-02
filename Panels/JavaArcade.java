package Panels;

public interface JavaArcade {
    boolean running();
    void startGame();
    String getGameName();
    void pauseGame();
    String getInstructions();
    String getCredits();
    String getHighScore();
    void stopGame();
    int getPoints();
}