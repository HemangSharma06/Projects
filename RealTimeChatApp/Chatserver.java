import java.io.*;
import java.net.*;
import java.util.*;

public class Chatserver {
    private static final int PORT = 5000;
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Map<String, PrintWriter> users = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started... waiting for the clients....");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("0.0.0.0"));
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Enter username: ");
                username = in.readLine();
                users.put(username, out);

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                // Notify all clients that this user is online
                sendToAll(username + " has joined the chat!");

                // Listen for messages from the client
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/exit")) {
                        break;
                    }
                    sendToAll(username + ": " + message);
                }

                // Cleanup when user exits
                users.remove(username);
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }

                sendToAll(username + " has left the chat.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void sendToAll(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
}
