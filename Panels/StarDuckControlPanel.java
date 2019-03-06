package Panels;

import javax.swing.*;
import java.awt.*;

public class StarDuckControlPanel extends JPanel {

    private final String MENU = "Main Menu", GAME = "Game";
    private JFrame parent;
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private CardLayout cardLayout;

    public static enum GameType {
        LOCAL,
        LAN
    }

    public StarDuckControlPanel(JFrame parent) {
        this.parent = parent;
        this.mainMenuPanel = new MainMenuPanel(786, 456, this);
        this.cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        this.add(mainMenuPanel, MENU);
    }

    public void showMenu() {
        cardLayout.show(this, MENU);
        cardLayout.removeLayoutComponent(gamePanel);
        parent.setSize(786, 488);
        mainMenuPanel.reactivate();
    }

    public void startGame(GameType gameType) {
        this.gamePanel = new GamePanel(786, 456, gameType, this);
        this.add(gamePanel, GAME);
        cardLayout.show(this, GAME);
    }
}
