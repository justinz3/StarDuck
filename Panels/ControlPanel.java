// Represents a control panel for the arcade

package Panels;

import Panels.JavaArcade;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Container;
import javax.swing.Box;
import javax.swing.*;

public class ControlPanel extends JPanel implements ActionListener {
    private JavaArcade game;
    private GameStats gStats;
    private JButton startButton, pauseButton, stopButton, instructionsButton, creditsButton;

    // Constructor
    public ControlPanel(JavaArcade t, GameStats g) {
        game = t;
        gStats = g;

        instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(this);
        add(instructionsButton);
        add(Box.createHorizontalStrut(80));
        startButton = new JButton("Start");
        startButton.addActionListener(this);

        add(startButton);

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        add(pauseButton);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        add(stopButton);
        add(Box.createHorizontalStrut(80));
        creditsButton = new JButton("Credits");
        creditsButton.addActionListener(this);
        add(creditsButton);

        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    // Called when the start button is clicked
    public void actionPerformed(ActionEvent e) {

        JButton button = (JButton) e.getSource();

        if (button == startButton) {
            if (!game.running()) {
                game.startGame();
                gStats.repaint();
            }
        } else if (button == pauseButton) {
            game.pauseGame();
            pauseButton.setText(game.running() ? "Pause" : "Resume");
            repaint();
        } else if (button == stopButton) {
            if (game.running()) {
                game.stopGame();
                gStats.gameOver(game.getPoints());
                gStats.repaint();
                repaint();
            }
        } else if (button == creditsButton) {
            String credits = game.getCredits();
            JOptionPane.showMessageDialog(this, credits, "Game Credits", JOptionPane.PLAIN_MESSAGE);

        } else if (button == instructionsButton) {
            String instructions = game.getInstructions();
            JOptionPane.showMessageDialog(this, instructions, "Game Rules", JOptionPane.PLAIN_MESSAGE);

        }
        ((JPanel) (game)).requestFocus();
    }

    private void enable(JButton button) {
        button.setEnabled(true);
    }

    private void disable(JButton button) {
        button.setEnabled(false);
    }

    public void enableStartButton() {
        enable(startButton);
    }

    public void disableStartButton() {
        disable(startButton);
    }

    public void enablePauseButton() {
        enable(pauseButton);
    }

    public void disablePauseButton() {
        disable(pauseButton);
    }

    public void resetPauseButton() {
        pauseButton.setText("Pause");
    }

    public void enableStopButton() {
        enable(stopButton);
    }

    public void disableStopButton() {
        disable(stopButton);
    }

    public void enableInstructionButton() {
        enable(instructionsButton);
    }

    public void disableInstructionsButton() {
        disable(instructionsButton);
    }

    public void enableCreditsButton() {
        enable(creditsButton);
    }

    public void disableCreditsButton() {
        disable(creditsButton);
    }
}


