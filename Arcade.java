import java.awt.Container;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

import Panels.*;

public class Arcade extends JFrame {
    public Arcade() {
        super("AP Java Arcade");

        JavaArcade game = new StarDuckControlPanel(this);

        GameStats display = new GameStats(game); //passing in a JavaArcade, therefore I know I can call getHighScore(), getScore()


        ControlPanel controls = new ControlPanel(game, display); //Also passing in JavaArcade to ControlPanel, I know you will respond to buttons

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 5, 0, 5));
        panel.add(display, BorderLayout.NORTH);
        panel.add((JPanel) game, BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);

        Container c = getContentPane();
        c.add(panel, BorderLayout.CENTER);

        setSize(768, 575);
        setResizable(false);
    }

    public static void main(String[] args) {
        Arcade window = new Arcade();
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}

