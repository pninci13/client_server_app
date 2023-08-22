import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static int clientCounter = 0;

    public static void main(String[] args) {
        int portNumber = 12345; // Change this to your desired port number
        ExecutorService executorService = Executors.newFixedThreadPool(5); // Allow up to 5 clients

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getNextClientId() {
        return ++clientCounter;
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private int clientId;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientId = Server.getNextClientId();
    }

    @Override
    public void run() {
        try {
            // Create a unique file for each client
            String clientFileName = "client_" + clientId + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(clientFileName));

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Received from client " + clientId + ": " + inputLine);
                writer.write(inputLine);
                writer.newLine();
                writer.flush();
            }

            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
