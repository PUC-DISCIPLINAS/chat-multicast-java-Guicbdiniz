package app;

import server.ChatServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        System.out.println("Starting server");
        try {
            ChatServer chatServer = ChatServer.getInstance();
            while (true) {
                chatServer.listenToClient();
            }
        } catch (IOException e) {
            System.out.println("Server IOException caught");
            e.printStackTrace();
        }
    }
}
