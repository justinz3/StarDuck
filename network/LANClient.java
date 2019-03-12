package network;

import java.net.*;
import java.io.*;

public class LANClient implements LANFunctions {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws Exception {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public boolean checkConnection() throws Exception {
        out.println("Pinging server");

        boolean valid = "Ping returned".equals(in.readLine());
        System.out.println(valid ? "Connected to server" : "Failed to connect to server");
        return valid;
    }

    public void sendMessage(String message) throws Exception {
        out.println(message);
    }

    public String receiveMessage() throws Exception {
        return in.readLine();
    }

    public void stopConnection() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
    }

}
