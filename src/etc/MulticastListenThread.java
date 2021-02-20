package etc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastListenThread extends Thread {
    private final DataOutputStream out;
    private final MulticastSocket multicastSocket;

    public MulticastListenThread(DataOutputStream out, int localPort, InetAddress host) throws IOException {
        this.out = out;
        this.multicastSocket = new MulticastSocket(localPort);
        multicastSocket.joinGroup(host);
        this.start();
    }

    public void run() {
        byte[] buffer = new byte[1000];
        DatagramPacket message;
        try {
            do {
                System.out.println("Receiving message!");
                message = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(message);
                out.writeUTF(new String(message.getData()).trim());
            } while (!this.isInterrupted());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (multicastSocket != null) {
                multicastSocket.close();
            }
        }
    }
}
