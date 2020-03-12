package v7;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("Duplicates")
public class Server {

    private static final ExecutorService executors =
            Executors.newFixedThreadPool(10, r -> new Thread(r, "server-threads"));

    public static void main(String[] args) throws IOException {
        int portNumber = 8081;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                RequestProcessor requestProcessor = new RequestProcessor(executors, clientSocket);
                executors.execute(requestProcessor);
            }
        }
    }
}
