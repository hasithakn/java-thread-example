package v7;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@SuppressWarnings("Duplicates")
public class RequestProcessor implements Runnable {

    private final ExecutorService executorService;
    private Socket clientsSocket;


    public RequestProcessor(ExecutorService executorService, Socket socket) {
        this.executorService = executorService;
        this.clientsSocket = socket;
    }

    private void process(Socket socket) {
        try (
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {

            String inputLine;

            if ((inputLine = in.readLine()) != null) {
                System.out.println(String.format("Received Client Message is [%s]", inputLine));
                CloseableHttpClient httpClient = HttpClients.createDefault();
                ProductDetailsService productDetailsService = new ProductDetailsService(new ApiRequestHandler(httpClient), executorService);
                out.println(productDetailsService.getDetails(inputLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        process(clientsSocket);
    }
}
