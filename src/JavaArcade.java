public interface JavaArcade {
    public boolean running();
    public void startGame();
    public String getGameName();
    public void pauseGame();
    public String getInstructions();
    public String getCredits();
    public String getHighScore();
    public void stopGame();
    public int getPoints();
}