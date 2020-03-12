package v7;


import util.LogPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

@SuppressWarnings("Duplicates")
public class SocketQueueImpl<T> implements SocketQueue<T> {

    private final LinkedList<T> items = new LinkedList<>();
    private int count = 0;
    private int throttle = 5;

    @Override
    public void newRequestReceived(T value) {
        LogPrinter.logMsg("New Request : " + value);
        synchronized (items) {
//            if (count < throttle) {
                items.offer(value);
                items.notifyAll();
                count++;
//            } else {
//                rejectRequest(value);
//            }
        }
    }

    private void rejectRequest(T value) {
        Socket socket = (Socket) value;
        try (
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                System.out.println(String.format("Received But Rejecting request, Client Message is [%s]", inputLine));
                out.println("Server is busy at the moment");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T getRequest() throws InterruptedException {
        synchronized (items) {
            do {
                if (!items.isEmpty()) {
                    count--;
                    return items.remove();
                }
                items.wait();
            } while (items.isEmpty());
            count--;
            return items.remove();
        }
    }
}
