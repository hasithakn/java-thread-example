package v7;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("Duplicates")
public class RequestProcessor {

    private final Thread t;
    private final SocketQueueImpl<Socket> socketStore;

    public RequestProcessor(int tId, SocketQueueImpl<Socket> socketStore) {
        this.socketStore = socketStore;
        t = new Thread(() -> process());
        t.setName("processor - "+tId);
        t.start();
    }


    private void process(){
        while (true) {
            try {
                Socket socket = socketStore.getRequest();
                doProcess(socket);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private void doProcess(Socket socket) {
        try (
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {

            String inputLine;

            if ((inputLine = in.readLine()) != null) {
                System.out.println(String.format("Received Client Message is [%s]",inputLine));
                CloseableHttpClient httpClient = HttpClients.createDefault();
                ProductDetailsService productDetailsService = new ProductDetailsService(new ApiRequestHandler(httpClient));
                out.println(productDetailsService.getDetails(inputLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
