package client;

import server.ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPClient {

    private final Socket clientSocket;
    private final DataOutputStream out;
    private final DataInputStream in;

    public TCPClient(String host) throws IOException {
        clientSocket = new Socket(host, ChatServer.getServerPort());
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public String interactWithServer(String message) throws IOException {
        out.writeUTF(message);
        String serverReturn = in.readUTF();
        return serverReturn;
    }

    public void closeConnection() throws IOException {
        clientSocket.close();
    }
}
