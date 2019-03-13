package Panels;

import Helpers.Helpers;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class StarDuckControlPanel extends JPanel implements JavaArcade {

    private final String MENU = "Main Menu", GAME = "Game";
    private JFrame parent;
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private CardLayout cardLayout;
    private ControlPanel controlPanel;
    private GameStats gameStats;

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
        if (gamePanel != null)
            cardLayout.removeLayoutComponent(gamePanel);
        parent.setSize(786, 488);
        mainMenuPanel.reactivate();
        currentlyVisible = PanelType.MENU;
        controlPanel.resetPauseButton();
        controlPanel.enableInstructionButton();
        controlPanel.enableStartButton();
        controlPanel.disablePauseButton();
        controlPanel.disableStopButton();
        controlPanel.enableCreditsButton();
    }

    public void startGame(GameType gameType) {
        this.gamePanel = new GamePanel(1572, 912, gameType, this);
        Helpers.linkGamePanel(gamePanel);
        parent.setSize(1572, 944);
//        this.gamePanel = new GamePanel(786, 456, gameType, this);
//        parent.setSize(786, 488);
        this.add(gamePanel, GAME);
        cardLayout.show(this, GAME);
        currentlyVisible = PanelType.GAME;
        controlPanel.disableStartButton();
        controlPanel.disableCreditsButton();
        controlPanel.disableInstructionsButton();
        controlPanel.enablePauseButton();
        controlPanel.enableStopButton();
    }

    public boolean running() {
        if (gamePanel == null)
            return false;
        return gamePanel.isRunning();
    }

    public void startGame() {
        if (gamePanel != null && gamePanel.isRunning())
            JOptionPane.showMessageDialog(this, "The game has already started!", "Uh Oh!", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(this, "Use the buttons on the screen to choose a game mode!", "Start Game", JOptionPane.PLAIN_MESSAGE);
    }

    public String getGameName() {
        return "StarDuck";
    }

    public void pauseGame() {
        if (currentlyVisible == PanelType.GAME) {
            gamePanel.togglePause();
            if (gamePanel.isRunning())
                controlPanel.enableStopButton();
            else
                controlPanel.disableStopButton();
        }
    }

    public String getInstructions() {
        return "INSTRUCTIONS:\n\nLOCAL GAME: \nPlayer One (Blue): use WASD to move + turn, Space to shoot a laser and Shift to shoot a bomb\n " +
                "Player Two (Green): use Numpad Arrow Keys to move + turn, Numpad 0 to shoot a laser and Numpad 1 to shoot a bomb\n\nLAN Game:\n" +
                "Use WASD to move + turn, Space to shoot a laser, and Shift to fire a bomb (you are always the blue ship).";
    }

    public String getCredits() {
        return "CREDITS:\n" +
                "\u00a9 2019 BA Games (Daniel Ivanovich and Justin Zhu)\n" +
                "Visit this project on GitHub at http://bit.ly/StarDuck";
    }

    public String getHighScore() {
        File highScore = new File("HighScore.txt");
        int score = Integer.MIN_VALUE;

        if (highScore.exists()) {
            Scanner scanner;
            try {
                scanner = new Scanner(highScore);

                score = scanner.nextInt();
                scanner.close();
            } catch (IOException e) {
                // Do nothing
            }
        }

        if (score == Integer.MIN_VALUE)
            return "None";
        else
            return score + " points";
    }

    public void stopGame() {
        if (currentlyVisible == PanelType.GAME) {
            gamePanel.endGame();
            Helpers.linkGamePanel(null);
        }
    }

    public int getPoints() {
        String[] scoreText = JavaArcade.getCurrentLeader().split(": ");
        if(scoreText.length < 2)
            return 0;
        return Integer.parseInt(scoreText[1].split(" p")[0]);
    }

    public void linkControlPanel(ControlPanel c) {
        this.controlPanel = c;
    }

    public void linkGameStats(GameStats gameStats) {
        this.gameStats = gameStats;
    }

    public void updateStats() {
        gameStats.update();
    }

    public void managePause(boolean running) {
        if(!running) {
            controlPanel.disablePauseButton();
            controlPanel.enableStartButton();
        } else {
            controlPanel.enablePauseButton();
            controlPanel.disableStartButton();
        }
    }
}
