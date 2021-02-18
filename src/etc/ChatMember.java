package etc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

public class ChatMember {

    private final String memberName;
    private final DataOutputStream outputStream;
    private final MulticastListenThread multicastListenThread;
    private static int nextLocalPort = 6789;

    public ChatMember(String memberName, DataOutputStream outputStream, InetAddress host) throws IOException {
        this.memberName = memberName;
        this.outputStream = outputStream;
        this.multicastListenThread = new MulticastListenThread(outputStream, nextLocalPort, host);
        nextLocalPort++;
    }

    public String getMemberName() {
        return memberName;
    }

    public void destroyMember() {
        multicastListenThread.interrupt();
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public static int useNextLocalPort() {
        return nextLocalPort++;
    }
}
