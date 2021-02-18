package etc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoom {

    private final InetAddress host;
    private final String chatName;
    private final List<ChatMember> members;
    private final MulticastSocket multicastSocket;
    private final int sendLocalPort;

    public ChatRoom(String host, String chatName) throws IOException {
        this.host = InetAddress.getByName(host);
        this.chatName = chatName;
        this.members = new ArrayList<>();
        this.sendLocalPort = ChatMember.useNextLocalPort();
        this.multicastSocket = new MulticastSocket(this.sendLocalPort);
        multicastSocket.joinGroup(this.host);
    }

    /**
     * Get multicast room host.
     */
    public InetAddress getHost() {
        return host;
    }


    /**
     * Get multicast room name.
     */
    public String getChatName() {
        return chatName;
    }

    /**
     * Get the name of each member.
     */
    public List<String> getMembersNames() {
        return members.stream().map(ChatMember::getMemberName).collect(Collectors.toList());
    }

    /**
     * Add member to chat room.
     */
    public void addMember(String memberName, DataOutputStream outputStream) throws IOException {
        members.add(new ChatMember(memberName, outputStream, host));
    }

    /**
     * Remove member from chat room.
     */
    public void removeMember(String memberName) {
        members.stream().filter(member -> memberName.equals(member.getMemberName())).forEach(ChatMember::destroyMember);
        members.removeIf(member -> memberName.equals(member.getMemberName()));
    }

    /**
     * Send message from passed member.
     */
    public void sendMessage(String memberName, String message) throws IOException {
        message = memberName + ": " + message + "\n";
        byte[] messageInBytes = message.getBytes();
        DatagramPacket messageOut = new DatagramPacket(messageInBytes, messageInBytes.length, host, sendLocalPort);
        multicastSocket.send(messageOut);
    }
}
