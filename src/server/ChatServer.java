package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    private static ChatServer serverInstance = null;
    private static final int SERVER_PORT = 7896;

    private final ServerSocket serverSocket;

    private ChatServer() throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);
    }

    /**
     * Get the chat server instance.
     * This class method is part of the Singleton pattern.
     *
     * @return ChatServer static instance.
     */
    public static ChatServer getInstance() throws IOException {
        if (serverInstance == null) {
            serverInstance = new ChatServer();
        }
        return serverInstance;
    }

    public void listenToClient() throws IOException {
        Socket clientSocket = serverSocket.accept();
        new Connection(clientSocket);
    }

    /**
     * Get chat server port.
     */
    public static int getServerPort() {
        return SERVER_PORT;
    }
}
