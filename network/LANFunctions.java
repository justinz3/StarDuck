package network;

public interface LANFunctions {
    boolean checkConnection() throws Exception;
    void sendMessage(String message) throws Exception;
    String receiveMessage() throws Exception;
}
