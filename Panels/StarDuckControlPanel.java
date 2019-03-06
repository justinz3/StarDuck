package Panels;

import javax.swing.*;
import java.awt.*;

public class StarDuckControlPanel extends JPanel implements JavaArcade {

    private final String MENU = "Main Menu", GAME = "Game";
    private JFrame parent;
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private CardLayout cardLayout;

    public enum GameType {
        LOCAL,
        LAN
    }

    private enum PanelType {
        MENU,
        GAME
    }

    private PanelType currentlyVisible;
    public StarDuckControlPanel(JFrame parent) {
        this.parent = parent;
        this.mainMenuPanel = new MainMenuPanel(786, 456, this);
        this.cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        this.add(mainMenuPanel, MENU);
        currentlyVisible = PanelType.MENU;
    }

    public void showMenu() {
        cardLayout.show(this, MENU);
        cardLayout.removeLayoutComponent(gamePanel);
        parent.setSize(786, 488);
        mainMenuPanel.reactivate();
        currentlyVisible = PanelType.MENU;
    }

    public void startGame(GameType gameType) {
        this.gamePanel = new GamePanel(1572, 912, gameType, this);
        parent.setSize(1572, 944);
        this.add(gamePanel, GAME);
        cardLayout.show(this, GAME);
        currentlyVisible = PanelType.GAME;
    }

    public boolean running() {
        if(gamePanel == null)
            return false;
        return gamePanel.isRunning();
    }

    public void startGame() {
        if(gamePanel != null && gamePanel.isRunning())
            JOptionPane.showMessageDialog(this, "The game has already started!", "Uh Oh!", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(this, "Use the buttons on the screen to choose a game mode!", "Start Game", JOptionPane.PLAIN_MESSAGE);
    }

    public String getGameName() {
        return "StarDuck";
    }

    public void pauseGame() {
        if(currentlyVisible == PanelType.GAME)
            gamePanel.togglePause();
    }

    public String getInstructions() {
        return "INSTRUCTIONS";
    }

    public String getCredits() {
        return "CREDITS: Dan and Justin";
    }

    public String getHighScore() {
        return "High Score: 0";
    }

    public void stopGame() {
        if(currentlyVisible == PanelType.GAME)
            gamePanel.endGame();
    }

    public int getPoints() {
        return 0;
    }
}
