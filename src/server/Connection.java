package etc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
    private final DataInputStream in;
    private final DataOutputStream out;
    private Socket clientSocket;

    public Connection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        this.start();
    }

    public void run() {
        try {
            getMessagesFromClient();
        } catch (EOFException e) {
            System.out.println("EOF exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO exception: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Server: a client connection was closed.");
            } catch (IOException e) {
                System.out.println("There was an error while trying to close a client connection.");
            }
        }
    }

    public void getMessagesFromClient() throws IOException {
        String messageReceived = in.readUTF();
        String[] messageWords = messageReceived.split(" ");

        while (true) {
            switch (messageWords[0]) {
                case "list":

            }
        }
        System.out.println("Message received from: " + clientSocket.getLocalAddress());
        out.writeUTF("Deu bom");
    }
}
