package edu.duke.ece568.mini_ups.service.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketService {
    public OutputStream out;
    public InputStream in;
    public void startClient(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to server at " + host + ":" + port);
            out = socket.getOutputStream();
            in = socket.getInputStream();
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
