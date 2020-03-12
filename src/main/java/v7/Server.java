package v7;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("Duplicates")
public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 8081;
        SocketQueueImpl socketStore = new SocketQueueImpl();
        RequestCoordinator rc = new RequestCoordinator(socketStore);
        rc.start();
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                socketStore.newRequestReceived(clientSocket);
            }
        }
    }
}
