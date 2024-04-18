package edu.duke.ece568.mini_ups.service.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketService {
    public PrintWriter out;
    public BufferedReader in;
    public void startClient(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to server at " + host + ":" + port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void stopClient() {
        try {
            out.close();
            in.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
