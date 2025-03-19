import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;

public class ChatClientGUI extends Application {
    private static final String SERVER_ADDRESS = "Enter ip of Server laptop";
    private static final int SERVER_PORT = 12345;
    
    private TextArea messageArea;
    private TextField inputField;
    private Button sendButton;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    @Override
    public void start(Stage primaryStage) {
        // UI components
        messageArea = new TextArea();
        messageArea.setEditable(false);

        inputField = new TextField();
        sendButton = new Button("Send");

        // Link Enter key explicitly to Send button
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });

        // Link Send button click
        sendButton.setOnAction(_ -> sendMessage());

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(messageArea, inputField, sendButton);

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Chat App");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(_ -> closeConnection());
        primaryStage.show();

        // Connect to server
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            // Read messages in a separate thread
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            messageArea.appendText("Failed to connect to server.\n");
        }
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                String finalMessage = message;
                javafx.application.Platform.runLater(() -> messageArea.appendText(finalMessage + "\n"));
            }
        } catch (IOException e) {
            messageArea.appendText("Connection lost.\n");
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty() && out != null) {
            out.println(message);
            inputField.clear();
        }
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
