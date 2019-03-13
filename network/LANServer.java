package network;

import Player.KeyInputSet;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class LANServer extends KeyBoardMapper implements LANFunctions  {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public boolean checkConnection() throws Exception {
        boolean valid = "Pinging server".equals(in.readLine());

        System.out.println(valid ? "Player connected to server" : "Player failed to connect to server");
        out.println(valid ? "Ping returned" : "Incorrect message!");

        return valid;
    }

    public void sendMessage(String message) throws Exception {
        out.println(message);
    }

    public String receiveMessage() throws Exception {
        return in.readLine();
    }

    public void stop() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    @Override
    public void sendKeyboardStatus(HashMap<Integer, Boolean> keyboardMap, KeyInputSet inputSet) throws Exception {
        sendMessage(getKeyboardData(keyboardMap, inputSet));
    }

    @Override
    public boolean[] getKeyboardStatus() throws Exception {
        return interpretKeyboardData(receiveMessage());
    }
}
