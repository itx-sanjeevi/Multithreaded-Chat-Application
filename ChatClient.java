import java.io.*;
import java.net.*;

// Client Class
public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            PrintWriter writer =
                    new PrintWriter(
                            socket.getOutputStream(), true);

            BufferedReader console =
                    new BufferedReader(
                            new InputStreamReader(System.in));

            // Thread to receive messages
            new Thread(() -> {

                String msg;

                try {
                    while ((msg = reader.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();

            // Send messages
            String userInput;

            while ((userInput = console.readLine()) != null) {

                writer.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}