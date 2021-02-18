package app;

import client.TCPClient;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        TCPClient client = null;
        Scanner clientIn = new Scanner(System.in);
        String clientCommand;
        try {
            client = new TCPClient("127.0.0.1");
            while (true) {
                clientCommand = clientIn.nextLine();
                String returned = client.interactWithServer(clientCommand.strip());
                System.out.println("Returned: " + returned);
            }

        } catch (IOException e) {
            System.out.println("Client IOException caught");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.closeConnection();
            }

        }
    }
}
