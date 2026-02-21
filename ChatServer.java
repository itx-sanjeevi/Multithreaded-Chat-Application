import java.io.*;
import java.net.*;
import java.util.*;

// Main Server Class
public class ChatServer {

    private static final int PORT = 1234;

    // List to store all connected clients
    private static final Set<ClientHandler> clientHandlers =
            Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {

        System.out.println("Chat Server Started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {

                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                ClientHandler clientHandler =
                        new ClientHandler(socket);

                clientHandlers.add(clientHandler);

                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    public static void broadcast(String message, ClientHandler sender) {

        synchronized (clientHandlers) {

            for (ClientHandler client : clientHandlers) {

                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    // Remove client
    public static void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
    }
}