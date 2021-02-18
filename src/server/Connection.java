package server;

import etc.ChatMember;
import etc.ChatRoom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Connection extends Thread {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Socket clientSocket;
    private final ChatRoomsManager chatRoomsManager;

    public Connection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        this.chatRoomsManager = ChatRoomsManager.getInstance();
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

        if (!checkIfFirstReceivedMessageIsValid(messageWords)) {
            out.writeUTF("Invalid first message. Type 'login yourUserName'. Bye!");
            return;
        }

        String userName = messageWords[1];
        out.writeUTF("You are now connected!\n");

        boolean shouldExitLoop = false;
        while (!shouldExitLoop) {
            messageReceived = in.readUTF();
            messageWords = messageReceived.split(" ");

            switch (messageWords[0]) {
                case "list" -> listChatRooms();
                case "create" -> createChatRoom(messageWords[1]);
                case "enter" -> addClientToChatRoom(userName, messageWords[1]);
                case "send" -> sendMessageToChatRoom(
                        userName, messageWords[1], Arrays.copyOfRange(messageWords, 2, messageWords.length)
                );
                case "leave" -> removeClientFromChatRoom(userName, messageWords[1]);
                case "help" -> printHelp();
                case "exit" -> {
                    out.writeUTF("Exiting. Bye, " + userName);
                    shouldExitLoop = true;
                }
                default -> printUnknownCommandMessage();
            }
        }
    }

    /**
     * Validate login message (first message from client).
     */
    private boolean checkIfFirstReceivedMessageIsValid(String[] messageWords) {
        return messageWords.length == 2 && messageWords[0].equalsIgnoreCase("login");
    }

    private void listChatRooms() throws IOException {
        List<ChatRoom> chatRooms = chatRoomsManager.listRooms();
        String chatRoomsString = chatRooms.size() > 0 ? getChatRoomsString(chatRooms) : "No rooms available";
        out.writeUTF("Rooms:\n" + chatRoomsString);
    }

    /**
     * Create a readable string with info from each available room.
     */
    private static String getChatRoomsString(List<ChatRoom> chatRooms) {
        StringBuilder chatRoomsString = new StringBuilder();
        for (ChatRoom chatRoom : chatRooms) {
            chatRoomsString.append("Name: ")
                    .append(chatRoom.getChatName())
                    .append("\tIP: ")
                    .append(chatRoom.getHost())
                    .append("\n");
        }
        return chatRoomsString.toString();
    }

    private void createChatRoom(String chatRoomName) throws IOException {
        String chatRoomID = chatRoomsManager.getNextMulticastIP();
        chatRoomsManager.createRoom(chatRoomID, chatRoomName);
        out.writeUTF("Room created with name " + chatRoomName);
    }

    private void addClientToChatRoom(String clientName, String chatRoomName) throws IOException {
        ChatRoom selectedRoom = getChatRoomFromName(chatRoomName);
        if (selectedRoom == null) {
            out.writeUTF("Selected room does not exist. Please type it correctly");
            return;
        }
        selectedRoom.addMember(clientName, out);
        out.writeUTF("You are now connected to the room " + chatRoomName + "\n");
    }

    private void sendMessageToChatRoom(String clientName, String chatRoomName, String[] messageWords) throws IOException {
        ChatRoom selectedRoom = getChatRoomFromName(chatRoomName);
        if (selectedRoom == null) {
            out.writeUTF("Selected room does not exist. Please type it correctly");
            return;
        }
        if (!selectedRoom.getMembersNames().contains(clientName)) {
            out.writeUTF("You are not in this room. Join it first.");
            return;
        }
        selectedRoom.sendMessage(clientName, String.join(" ", messageWords));
        out.writeUTF("Message sent");
    }

    /**
     * Go through existent chat rooms and find the one with the passed name.
     * If the room is not found, null is returned.
     */
    private ChatRoom getChatRoomFromName(String chatRoomName) {
        List<ChatRoom> chatRooms = chatRoomsManager.listRooms();
        ChatRoom selectedRoom;
        selectedRoom = chatRooms.stream()
                .filter(chatRoom -> chatRoom.getChatName().equalsIgnoreCase(chatRoomName))
                .collect(Collectors.toList()).get(0);
        return selectedRoom;
    }

    private void removeClientFromChatRoom(String clientName, String chatRoomName) throws IOException {
        chatRoomsManager.listRooms()
                .stream()
                .filter(chatRoom -> chatRoom.getChatName().equals(chatRoomName))
                .forEach(chatRoom -> chatRoom.removeMember(clientName));

        out.writeUTF("You have left the room " + chatRoomName + "\n");
    }

    private void printHelp() throws IOException {
        String listCommandString = "list -> list available chat rooms\n";
        String createCommandString = "create chatRoomName -> create chat room\n";
        String enterCommandString = "enter chatRoomName -> enter chat room\n";
        String sendCommandString = "send chatRoomName message* -> send message to chat room\n";
        String leaveCommandString = "leave chatRoomName -> leave chat room\n";
        String helpCommandString = "help -> print this message\n";
        String exitCommandString = "exit -> exit the application\n";
        out.writeUTF("List of commands: \n" +
                listCommandString +
                createCommandString +
                enterCommandString +
                sendCommandString +
                leaveCommandString +
                helpCommandString +
                exitCommandString);
    }

    private void printUnknownCommandMessage() throws IOException {
        out.writeUTF("Invalid command. Type 'help' to see full list of valid commands.\n");
    }
}
