import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "Enter ip of Server laptop";
    private static final int SERVER_PORT = 12345;

    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader consoleReader;

    public static void main(String[] args) {
        new ChatClient().start();
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Read messages from the server in a separate thread
            Thread readThread = new Thread(new MessageReader());
            readThread.start();

            // Send messages to the server
            System.out.println("Enter your username: ");
            String username = consoleReader.readLine();
            out.println(username);

            // Send messages to server
            String message;
            while (true) {
                message = consoleReader.readLine();
                out.println(message);
                if (message.equals("/exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MessageReader implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
