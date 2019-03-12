package network;

import java.net.*;
import java.io.*;

public class LANServer implements LANFunctions {
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
}
