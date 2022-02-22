package kerzox.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private Socket socket;
    private DataHandling handler;

    @Override
    public void run() {
        try {
            Initialise(InetAddress.getLocalHost(), 2555);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Initialise(InetAddress Address, int port) throws IOException {
        this.socket = new Socket(InetAddress.getLocalHost(), port);
        System.out.println("Connected to server");
        this.handler = new DataHandling(this.socket);
    }

    public DataInputStream getServerInput() {
        return handler != null ? handler.getServerInput() : null;
    }

    public DataOutputStream getClientOutput() {
        return handler != null ? handler.getClientOutput() : null;
    }

    public Socket getSocket() {
        return socket;
    }


}
