package app;

import network.TCPClient;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            TCPClient client = new TCPClient("localhost");
            client.InteractWithServer("Hello World");
        } catch (IOException e) {
            System.out.println("IOException caught");
            e.printStackTrace();
        }
    }
}
