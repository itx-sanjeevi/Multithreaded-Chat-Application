import java.io.*;
import java.net.*;

// Client Handler Class
public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String clientName;

    public ClientHandler(Socket socket) {

        this.socket = socket;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            writer = new PrintWriter(
                    socket.getOutputStream(), true);

            writer.println("Enter your name:");
            clientName = reader.readLine();

            System.out.println(clientName + " joined");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        ChatServer.broadcast(
                clientName + " has joined the chat",
                this);

        String message;

        try {

            while ((message = reader.readLine()) != null) {

                String fullMessage =
                        clientName + ": " + message;

                System.out.println(fullMessage);

                ChatServer.broadcast(fullMessage, this);
            }

        } catch (IOException e) {
            System.out.println(clientName + " disconnected");
        } finally {

            try {
                socket.close();
            } catch (IOException e) {}

            ChatServer.removeClient(this);

            ChatServer.broadcast(
                    clientName + " left the chat",
                    this);
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}
