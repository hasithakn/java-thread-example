package v7;

import util.LogPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("Duplicates")
public class Client implements Runnable {

    private final String hostName;
    private final int portNumber;
    private final String message;

    public Client(String hostName, int portNumber, String message) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.message = message;
    }


    @Override
    public void run() {
        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()))
        ) {

            String fromServer;
            out.println(message);
            if ((fromServer = in.readLine()) != null) {
                System.out.println(fromServer);
            }
        } catch (IOException e) {
            LogPrinter.logMsg("Port is not available.");
        }
    }
}