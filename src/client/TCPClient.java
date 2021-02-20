package client;

import server.ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPClient {

    private final Socket clientSocket;
    private final DataOutputStream out;
    private final ClientListenThread clientListenThread;

    public TCPClient(String host) throws IOException {
        clientSocket = new Socket(host, ChatServer.getServerPort());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        clientListenThread = new ClientListenThread(in);
    }

    public void interactWithServer(String message) throws IOException {
        out.writeUTF(message);
    }

    public void closeConnection() throws IOException {
        clientListenThread.interrupt();
        clientSocket.close();
    }
}
