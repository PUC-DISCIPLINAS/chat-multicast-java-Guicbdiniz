package server;

import etc.ChatRoom;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomsManager {

    private static ChatRoomsManager managerInstance;
    private List<ChatRoom> chatRooms;
    private int nextMulticastIPIndex = 0;
    private String[] possibleMulticastIPs = new String[]{
            "228.5.6.7", "228.5.6.8", "228.5.6.9", "228.5.7.1", "228.5.7.2", "228.5.7.3", "228.5.7.4", "228.5.7.5",
            "228.5.7.6", "228.5.7.7", "228.5.7.8", "228.5.7.9", "228.5.8.1", "228.6.6.7", "228.7.6.7", "229.5.6.7",
            "229.8.6.7", "229.5.6.1", "229.5.6.2", "229.5.6.3"
    };

    private ChatRoomsManager() {
        chatRooms = new ArrayList<ChatRoom>();
    }

    /**
     * Get the chat manager instance.
     * This class method is part of the Singleton pattern.
     *
     * @return ChatRoomService static instance.
     */
    public static ChatRoomsManager getInstance() throws IOException {
        if (managerInstance == null) {
            managerInstance = new ChatRoomsManager();
        }
        return managerInstance;
    }

    /**
     * Get next multicast IP address and update it.
     */
    public String getNextMulticastIP() {
        String currentMulticastIP = possibleMulticastIPs[nextMulticastIPIndex];
        nextMulticastIPIndex++;
        return currentMulticastIP;
    }

    /**
     * Create a new multicast room.
     *
     * @param roomHost host's IP or identifier.
     * @param roomName name to identify the new room.
     * @throws UnknownHostException - if the passed host name is not found.
     */
    public void createRoom(String roomHost, String roomName) throws IOException {
        ChatRoom newRoom = new ChatRoom(roomHost, roomName);
        chatRooms.add(newRoom);
    }

    /**
     * Get list of created room's names.
     */
    public List<String> listRoomsNames() {
        return chatRooms.stream().map(ChatRoom::getChatName).collect(Collectors.toList());
    }

    public List<ChatRoom> listRooms() {
        return chatRooms;
    }
}
