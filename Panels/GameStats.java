// Represents current Game Stats

package Panels;

import Panels.JavaArcade;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameStats extends JPanel {
    private JTextField gameNameText, currentHighScorer, currentHighScore;
    private int yourScore;
    private JLabel yourScoreText, highScoreText;
    private JavaArcade game;

    // Constructor
    public GameStats(JavaArcade t) {
        super(new GridLayout(2, 4, 10, 0));
        setBorder(new EmptyBorder(0, 0, 5, 0));
        Font gameNameFont = new Font("Monospaced", Font.BOLD, 24);

        JLabel gName = new JLabel(" " + t.getGameName());

        gName.setForeground(Color.red);
        gName.setFont(gameNameFont);
        add(gName);

        highScoreText = new JLabel(" Current High Score:   " + t.getHighScore());
        add(highScoreText);

        add(new JLabel(" "));
        yourScoreText = new JLabel(" Current Leader: " + t.getCurrentLeader());

        add(yourScoreText);
        Font displayFont = new Font("Monospaced", Font.BOLD, 16);
        game = t;

    }

    public void gameOver(int points) {
        if (points > Integer.parseInt(game.getHighScore().split(" p")[0])) {
            yourScoreText.setForeground(Color.BLUE);
            String s = (String) JOptionPane.showInputDialog(this, "You are the new high scorer. Congratulations!\n Enter your name: ", "High Score", JOptionPane.PLAIN_MESSAGE, null, null, "name");
        }
    }

    public void update() {
        yourScoreText.setText(" Current Leader: " + game.getCurrentLeader());
        highScoreText.setText(" Current High Score: " + game.getHighScore());
    }
}
