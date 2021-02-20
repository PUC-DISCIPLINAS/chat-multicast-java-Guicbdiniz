package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public class ClientListenThread extends Thread {
    private final DataInputStream in;

    public ClientListenThread(DataInputStream in) {
        this.in = in;
        this.start();
    }

    public void run() {
        try {
            do {
                String messageFromServer = in.readUTF();
                System.out.println("Returned: " + messageFromServer);
            } while (!this.isInterrupted());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
