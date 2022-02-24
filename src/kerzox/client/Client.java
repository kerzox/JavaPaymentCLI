package kerzox.client;

import kerzox.common.ArgumentException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable, Serializable {

    private Socket socket;
    private DataHandling handler;
    private boolean closed = false;
    private TempData clientStuff;
    private int ID;

    @Override
    public void run() {
        if (socket == null) Initialise();
        while(!isClosed()) runClient();
        System.out.println("You have been disconnected");
    }

    private void runClient() {
        try {
            this.handler.doArgumentHandler(new Scanner(System.in));
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Initialise() {
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), 2555);
            System.out.println("Connected to server");
            this.handler = new DataHandling(this);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Socket getServer() {
        return socket;
    }

    public void close() {
        try {
            this.socket.close();
            this.closed = true;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public TempData getData() {
        return clientStuff;
    }

    public void refreshClientData(TempData clientStuff) {
        if (clientStuff.getId() != this.ID) return;
        this.clientStuff = clientStuff;
    }

    public void createClient(TempData clientStuff) {
        this.clientStuff = clientStuff;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
