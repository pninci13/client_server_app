import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change this to the server's address
        int serverPort = 12345; // Change this to the server's port

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");
            System.out.println("Enter text to send to the server. Type 'exit' to quit.");

            String userInput;
            while (true) {
                userInput = reader.readLine();
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }
                writer.write(userInput);
                writer.newLine();
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
