package Panels;

import javax.swing.*;
import java.awt.*;

import Physics.*;
import Physics.Vector;
import Player.*;
import Weapon.*;
import Helpers.*;
import network.*;

import java.awt.event.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GamePanel extends JPanel {

    private ArrayList<Interactable> objects;
    private static ArrayList<Player> players;
    public static ArrayList<Interactable> toBeAdded;
    public static ArrayList<Drawable> toBeDrawn;
    public static ArrayList<Double> playerScores = new ArrayList<>();
    public static boolean running;

    private Image backgroundImage;
    private int width, height;
    private int cornerDisplacementX, cornerDisplacementY;

    private int time;
    private javax.swing.Timer timer;
    private StarDuckControlPanel.GameType gameType;
    private StarDuckControlPanel controlPanel;
    private KeyboardListener keyListener;
    private HashMap<Integer, Boolean> isPressed;
    public static final int refreshPeriod = 5; // milliseconds

    private MouseListener mouseListener;
    private boolean serverReady = false;
    private LANRole gameRole;
    private LANServer server;
    private LANClient client;

    private enum LANRole {
        HOST,
        CLIENT
    }

    public GamePanel(int width, int height, StarDuckControlPanel.GameType gameType, StarDuckControlPanel controlPanel) {
        this.setFocusable(true);
        this.requestFocus();
        this.controlPanel = controlPanel;

        getFocus();

        this.gameType = gameType;
        this.backgroundImage = Helpers.getImage("graphics/stars-scrolling-large.gif");

        initPanelSize(width, height);

        //initMenu();

        initTimer();

        this.keyListener = new KeyboardListener();
        addKeyListener(keyListener);

        this.mouseListener = new ClickListener();
        addMouseListener(mouseListener);

        toBeAdded = new ArrayList<>();
        toBeDrawn = new ArrayList<>();
        objects = new ArrayList<>();
        players = new ArrayList<Player>();
        if (gameType == StarDuckControlPanel.GameType.LOCAL) {
            addPlayer(new Player(new Ship(Ship.ShipType.BLUE, this.getSize(), 0), KeyInputSet.WASD, false));
            addPlayer(new Player(new Ship(new Vector(width - 300, height - 300), new Vector(), new Vector(),
                    new Laser(2), new Bomb(2), Ship.ShipType.GREEN, this.getSize(), 1),
                    KeyInputSet.NUMPAD, false));
        } else {
            int role;
            Object[] options = {"Server Host", "Server Client", "Quit"};
            while (true) {
                role = JOptionPane.showOptionDialog(null, "What is your role in this game?", "Define role", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                if (role == JOptionPane.CLOSED_OPTION)
                    JOptionPane.showMessageDialog(null, "Sorry, but you have to choose an option!\nIf you clicked LAN by mistake, click quit!", "Uh oh!", JOptionPane.ERROR_MESSAGE);
                else if (role == JOptionPane.CANCEL_OPTION)
                    System.exit(-1);
                else
                    break;
            }

            gameRole = (role == JOptionPane.YES_OPTION) ? LANRole.HOST : LANRole.CLIENT;

            if (gameRole == LANRole.HOST) {
                addPlayer(new Player(new Ship(Ship.ShipType.BLUE, this.getSize(), 0), KeyInputSet.WASD, false));
                addPlayer(new Player(new Ship(new Vector(width - 300, height - 300), new Vector(), new Vector(),
                        new Laser(2), new Bomb(2), Ship.ShipType.RED, this.getSize(), 1),
                        null, false));
                server = new LANServer();
                try {
                    server.start(6666);
                    server.checkConnection();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Unable to create server!\nExiting...", "Uh oh!", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }

                serverReady = true;
            } else {
                addPlayer(new Player(new Ship(Ship.ShipType.RED, this.getSize(), 0), null, false));
                addPlayer(new Player(new Ship(new Vector(width - 300, height - 300), new Vector(), new Vector(),
                        new Laser(2), new Bomb(2), Ship.ShipType.BLUE, this.getSize(), 1),
                        KeyInputSet.WASD, false));

                String ip, localhost = "127.0.0.1";
                ip = JOptionPane.showInputDialog(null, "Please enter the IP of the server (leave blank for localhost)", "Connect to a game", JOptionPane.QUESTION_MESSAGE);
                if (ip == null || ip.length() < 7 || ip.length() > 45) {
                    JOptionPane.showMessageDialog(null, "IP entered was either too short, too long, or blank...\nConnecting to localhost...", "Uh oh!", JOptionPane.ERROR_MESSAGE);
                    ip = localhost;
                }
                client = new LANClient();
                try {
                    client.startConnection(ip, 6666);
                    client.checkConnection();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Unable to open connection at " + ip + ":6666!\nExiting...", "Uh oh!", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }

                serverReady = true;
            }
        }

        running = true;

        isPressed = new HashMap<>();
    }

    private void initPanelSize(int width, int height) {
        setSize(width, height);
        setLayout(null);

        this.width = width;
        this.height = height;
    }

    private void initTimer() {
        time = 0;
        timer = new javax.swing.Timer(refreshPeriod, new TimerListener(refreshPeriod));
        timer.start();
    }

    private void addPlayer(Player player) {
        players.add(player);
        objects.add(player.getShip());
        playerScores.add(0.0);
    }

    // ---------------------------------------------------------

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        g.drawImage(backgroundImage, cornerDisplacementX, cornerDisplacementY, this);

        for (Interactable object : objects) {
            object.draw(g);
        }
//        for (Drawable object : toBeDrawn) {
//            if(object instanceof Explosion) {
//                ((Explosion) object).draw(g, this);
//                continue;
//            }
//            object.draw(g);
//        }
    }


    // ---------------------------------------------------------


    private class TimerListener implements ActionListener {

        private int delay;

        public TimerListener(int delay) {
            this.delay = delay;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                int localTeam = gameRole == LANRole.HOST ? 0 : 1, awayTeam = 1 - localTeam;
                if (gameType == StarDuckControlPanel.GameType.LAN) {
                    if(!serverReady)
                        return;
                    else if (gameRole == LANRole.HOST)
                        server.sendKeyboardStatus(isPressed, players.get(localTeam).input);
                    else
                        client.sendKeyboardStatus(isPressed, players.get(localTeam).input);
                }

                if (isRunning()) {
                    time += delay;

                    // Clear dead players
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getShip().isDead()) {
                            players.remove(players.get(i));
                            i--;
                        }
                    }

                    // Clear destroyed objects
                    for (int i = 0; i < objects.size(); i++) {
                        if (objects.get(i).isDead()) {
                            objects.remove(i);
                            i--;
                        }
                    }

                    // Add created objects
                    while (toBeAdded.size() > 0) {
                        objects.add(toBeAdded.get(0));
                        toBeAdded.remove(0);
                    }

                    // Set Player movement
                    for (Player player : players) {

                        if (gameType == StarDuckControlPanel.GameType.LOCAL || player.getTeam() == localTeam) {
                            movePlayerFromMap(player);
                        } else {
                            // Away team player
                            boolean[] keyboardStatus = gameRole == LANRole.HOST ? server.getKeyboardStatus() : client.getKeyboardStatus();
                            running = keyboardStatus[6];

                            if(!running) {
                                controlPanel.managePause(running);
                                continue;
                            }

                            if (keyboardStatus[0])
                                player.moveForward();
                            else if (keyboardStatus[2])
                                player.moveBackward();
                            else {
                                player.zeroVelocity();
                            }

                            if (keyboardStatus[1]) {
                                if (player.isStrafing())
                                    player.strafeLeft();
                                else
                                    player.turnLeft();
                            } else if (keyboardStatus[3]) {
                                if (player.isStrafing())
                                    player.strafeRight();
                                else
                                    player.turnRight();
                            }

                            if (keyboardStatus[4]) {
                                Projectile proj = player.fire(0);
                                if (proj != null)
                                    objects.add(proj);
                            }

                            if (keyboardStatus[5]) {
                                Projectile proj = player.fire(1);
                                if (proj != null)
                                    objects.add(proj);
                            }
                        }

                        player.move();

                        player.updateTimeSinceLastFire(delay);
                        player.updateTimeSinceLastDamage(delay);
                    }

                    // Move everything and Check for projectiles outside the screen
                    ArrayList<Interactable> objectsToRemove = new ArrayList<>();
                    for (Interactable object : objects) {
                        object.move();

                        if (object instanceof Projectile) {
                            Projectile projectile = (Projectile) object;
                            Vector position = projectile.getPosition();
                            Dimension size = projectile.getImageSize();

                            if (position.getX() <= -1 * size.getWidth() || position.getY() <= -1 * size.getHeight() ||
                                    position.getX() >= getWidth() + size.getWidth() || position.getY() >= getHeight() + size.getHeight())
                                objectsToRemove.add(object);
                        }
                    }

                    // Avoids ConcurrentModificationExceptions
                    for (Interactable object : objectsToRemove)
                        objects.remove(object);


                    // Check for Collisions
                    for (int i = 0; i < objects.size(); i++) {
                        for (int j = 0; j < objects.size(); j++) {
                            if (i == j)
                                continue;
                            if (i < 0)
                                break;
                            if (j < 0)
                                continue;

                            //System.out.println(i + " " + j);
                            Hittable a = objects.get(i), b = objects.get(j);

                            if (a.getTeam() == b.getTeam())
                                continue;

                            if (a.getHitbox().isTouching(b.getHitbox())) {

                                boolean bIsDead = a.impact(b);
                                boolean aIsDead = b.impact(a);

                                int deltaI = 0, deltaJ = 0;
                                if (bIsDead) {
                                    objects.remove(b);
                                    if (i > j)
                                        deltaI--;
                                    deltaJ--;
                                }
                                if (aIsDead) {
                                    objects.remove(a);
                                    if (j > i)
                                        deltaJ--;
                                    deltaI--;
                                }

                                i += deltaI;
                                j += deltaJ;
                            }
                        }

                    }

                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error communicating between server and client!\nExiting...", "Uh oh!", JOptionPane.ERROR_MESSAGE);
                endGame();
            }
        }
    }

    public void endGame() {
        try {
            if (gameRole == LANRole.HOST)
                server.stop();
            else
                client.stopConnection();
        } catch (Exception e) {
            System.out.println("Error disconnecting...");
        }
        removeKeyListener(keyListener);

        JOptionPane.showMessageDialog(null, JavaArcade.getCurrentLeader(), "The Winner!", JOptionPane.PLAIN_MESSAGE);

        controlPanel.showMenu();
        running = false;

        manageHighScores();
        updateScores();

        objects.clear();
        players.clear();
        toBeAdded.clear();
        toBeDrawn.clear();
        playerScores.clear();

        manageHighScores();
        updateScores();
    }

    public class KeyboardListener implements KeyListener {

        public KeyboardListener() {
            super();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                endGame();

            if (running) {
                isPressed.put(e.getKeyCode(), true);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println(e);
            //System.out.println("Typed: " + e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            isPressed.put(e.getKeyCode(), false);
        }
    }

    private class ClickListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
//            if (gameType == StarDuckControlPanel.GameType.LAN) {
//                int x = e.getX();
//                int y = e.getY();
//
//                int n = objects.size();
//                for (int i = 0; i < n; i++) {
//                    Drawable object = objects.get(i);
//                    if (object instanceof Player) {
//                        Player player = (Player) object;
//                        Projectile proj = player.getShip().fire();
//                        objects.add(proj);
//                    }
//                }
//            }
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }
    }

    public boolean isRunning() {
        return running;
    }

    public void getFocus() {
        // Panel focusing code taken from: https://bit.ly/2EHLuHs
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                GamePanel.this.requestFocusInWindow();
            }
        });
    }

    public void togglePause() {
        running = !running;
        removeKeyListener(keyListener);
        if (running) {
            getFocus();
            addKeyListener(keyListener);
        }
        stopAllPlayers(); // Fixes bug where holding a key then pausing and resuming makes the ship move in one direction indefinitely
    }

    private void stopAllPlayers() {
        for (Player player : players) {
            player.setVelocity(new Vector(0, 0));
        }
    }

    private void manageHighScores() {
        File highScore = new File("HighScore.txt");
        int previousHighScore = Integer.MIN_VALUE;

        if (highScore.exists()) {
            Scanner scanner;
            try {
                scanner = new Scanner(highScore);
                previousHighScore = scanner.nextInt();
                scanner.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
        for (Player player : players) {
            int playerScore = (int) (playerScores.get(player.getTeam()) + 0.5);
            if (playerScore > previousHighScore) {
                PrintWriter writer;
                try {
                    writer = new PrintWriter(highScore);
                    writer.println(playerScore);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static String getColorByTeam(int team) {
        return players.get(team).getShipColor();
    }

    public void updateScores() {
        controlPanel.updateStats();
    }

    private void movePlayerFromMap(Player player) {
        if (isPressed.getOrDefault(player.input.getForward(), false))
            player.moveForward();
        else if (isPressed.getOrDefault(player.input.getBackward(), false))
            player.moveBackward();
        else {
            player.zeroVelocity();
        }

        if (isPressed.getOrDefault(player.input.getLeft(), false)) {
            if (player.isStrafing())
                player.strafeLeft();
            else
                player.turnLeft();
        } else if (isPressed.getOrDefault(player.input.getRight(), false)) {
            if (player.isStrafing())
                player.strafeRight();
            else
                player.turnRight();
        }

        if (isPressed.getOrDefault(player.input.getPrimaryShoot(), false)) {
            Projectile proj = player.fire(0);
            if (proj != null)
                objects.add(proj);
        }

        if (isPressed.getOrDefault(player.input.getSecondaryShoot(), false)) {
            Projectile proj = player.fire(1);
            if (proj != null)
                objects.add(proj);
        }
    }
}
